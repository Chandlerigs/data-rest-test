package com.igs.ematf.dataresttest.service;

import com.igs.ematf.dataresttest.model.WikiDocument;
import org.opensearch.client.opensearch.indices.DeleteIndexResponse;
import org.opensearch.client.opensearch.indices.IndexSettings;

import java.util.List;

public interface OpenSearchService {

    /**
     * 创建索引
     */
    boolean indexExists(String index);

    /**
     * 创建索引
     */
    void createIndex(String index);

    /**
     * 设置索引
     */
    void setIndexSetting(IndexSettings settings);

    void setIndexSetting(String index, IndexSettings settings);

    /**
     * 同步数据到 opensearch
     */
    boolean syncWikiData();

    /**
     * 删除索引
     */
    DeleteIndexResponse deleteIndex(String id);

    DeleteIndexResponse deleteIndex(String index, String id);

    /**
     * 更新索引
     */
    void updateIndex(WikiDocument doc);

    void updateIndex(String index, WikiDocument doc);

    /**
     * 批量操作
     */
    void bulkUpdateIndex(String index, List<WikiDocument> docs);

    /**
     * 搜索文档
     */
    List<WikiDocument> search(String keyword, int page, int size);

    List<WikiDocument> search(String index, String keyword, int page, int size);


}