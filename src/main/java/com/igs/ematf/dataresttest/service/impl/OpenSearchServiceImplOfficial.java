package com.igs.ematf.dataresttest.service.impl;

import com.igs.ematf.dataresttest.entity.Content;
import com.igs.ematf.dataresttest.model.WikiDocument;
import com.igs.ematf.dataresttest.repository.ContentRepository;
import com.igs.ematf.dataresttest.service.OpenSearchService;
import com.igs.ematf.dataresttest.util.WikiSearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.OpenSearchException;
import org.opensearch.client.opensearch._types.analysis.Analyzer;
import org.opensearch.client.opensearch._types.mapping.DateProperty;
import org.opensearch.client.opensearch._types.mapping.KeywordProperty;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch._types.mapping.TextProperty;
import org.opensearch.client.opensearch._types.query_dsl.MultiMatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.indices.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OpenSearchServiceImplOfficial implements OpenSearchService {
    private final OpenSearchClient client;
    private final ContentRepository contentRepository;
    @Value("${opensearch.index}")
    private String wikiIndex;
    @Value("${opensearch.timeout}")
    private String timeout;
    @Value("${opensearch.index}")
    private String openSearchIndex;

    public OpenSearchServiceImplOfficial(OpenSearchClient client, ContentRepository contentRepository) {
        this.client = client;
        this.contentRepository = contentRepository;
    }

    public boolean indexExists(String index) {
        try {
            return client.indices().exists(e -> e.index(index)).value();
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void createIndex(String indexName) {
        // 配置分析器
        Map<String, Analyzer> analyzers = new HashMap<>();
        analyzers.put("ik_max_word", Analyzer.of(a -> a
                .custom(c -> c.tokenizer("ik_max_word"))
        ));

        analyzers.put("ik_smart", Analyzer.of(a -> a
                .custom(c -> c.tokenizer("ik_smart"))
        ));

        // 构建索引设置
        IndexSettings settings = IndexSettings.of(s -> s
                .analysis(IndexSettingsAnalysis.of(a -> a
                        .analyzer(analyzers)
                ))
        );

        // 配置映射
        Map<String, Property> properties = new HashMap<>();
        properties.put("title", Property.of(p -> p
                .text(TextProperty.of(t -> t.analyzer("ik_smart")))
        ));

        properties.put("content", Property.of(p -> p
                .text(TextProperty.of(t -> t.analyzer("ik_max_word")))
        ));

        properties.put("author", Property.of(p -> p
                .keyword(KeywordProperty.of(k -> k))
        ));

        properties.put("type", Property.of(p -> p
                .keyword(KeywordProperty.of(k -> k))
        ));

        properties.put("createTime", Property.of(p -> p
                .date(DateProperty.of(d -> d.format("epoch_millis")))
        ));

        properties.put("updateTime", Property.of(p -> p
                .date(DateProperty.of(d -> d.format("epoch_millis")))
        ));

        // 创建索引请求
        CreateIndexRequest request = CreateIndexRequest.of(c -> c
                .index(indexName)
                .settings(settings)
                .mappings(m -> m.properties(properties))
        );

        // 执行创建索引
        CreateIndexResponse response = null;
        try {
            response = client.indices().create(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Boolean.TRUE.equals(response.acknowledged())) {
            log.info("索引创建成功: " + indexName);
        } else {
            log.error("索引创建失败: " + indexName);
        }
    }

    public void setIndexSetting(IndexSettings settings) {
        setIndexSetting(wikiIndex, settings);
    }

    public void setIndexSetting(String index, IndexSettings settings) {
        IndexSettings indexSettings = new IndexSettings.Builder()
                .index(settings)
                .autoExpandReplicas("0-all")
                .build();
        PutIndicesSettingsRequest putIndicesSettingsRequest =
                new PutIndicesSettingsRequest.Builder()
                        .index(index)
                        .settings(indexSettings)
                        .build();
        try {
            client.indices().putSettings(putIndicesSettingsRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 批量同步Wiki数据到OpenSearch
    public boolean syncWikiData() {
        // 获取所有Wiki数据
        List<Content> entities = contentRepository.findAll();
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();
        for (Content entity : entities) {
            WikiDocument doc = WikiSearchUtil.convertToDocument(entity);
            bulkRequest.operations(op -> op
                    .index(idx -> idx
                            .index(wikiIndex)
                            .id(doc.getId())
                            .document(doc)
                    )
            );
        }
        boolean result = true;
        List<String> errorIds = new ArrayList<>();
        BulkResponse response = null;
        try {
            response = client.bulk(bulkRequest.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response.errors()) {
            result = false;
            // 处理同步错误
            response.items().forEach(item -> {
                errorIds.add(item.id());
                log.error("Error indexing document: " + item.id());
                if (item.error() != null) {
                    log.error("Error indexing document: " + item.error().reason());
                }
            });
        }
        return result;
    }


    /**
     * 删除文档
     */
    public DeleteIndexResponse deleteIndex(String id) {
        return deleteIndex(wikiIndex, id);
    }

    public DeleteIndexResponse deleteIndex(String index, String id) {

        try {
            //Delete the document
            client.delete(b -> b.index(index).id(id));
            // Delete the index
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(index).build();
            return client.indices().delete(deleteIndexRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public void updateIndex(WikiDocument doc) {
        updateIndex(wikiIndex, doc);
    }

    public void updateIndex(String index, WikiDocument doc) {
        try {
            client.index(i -> i
                    .index(index)
                    .id(doc.getId())
                    .document(doc));
        } catch (IOException e) {
            log.error("Failed to update index id={}, type={}, message={}", doc.getId(), doc.getType(), e.getMessage());
        }
    }

    public void bulkUpdateIndex(String index, List<WikiDocument> docs) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (WikiDocument doc : docs) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(index)
                            .id(doc.getId())
                            .document(doc)));
        }
        try {
            client.bulk(br.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<WikiDocument> search(String keyword, int page, int size) {
        return search(wikiIndex, keyword, page, size);
    }

    public List<WikiDocument> search(String index, String keyword, int page, int size) {
        log.info("search, keyword:{}", keyword);
        SearchResponse<WikiDocument> searchResponse = null;
        try {
            Query query = MultiMatchQuery.of(t -> t
                    .fields("title", "content", "author")
                    .query(keyword)
            )._toQuery();
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(index)
                    .query(query)
                    .from((page - 1) * size)
                    .size(size)
            );
            searchResponse = client.search(searchRequest, WikiDocument.class);
        } catch (OpenSearchException | IOException e) {
            log.error("OpenSearchException: " + e.getMessage());
            return new ArrayList<>();
        }
        return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

}