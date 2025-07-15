package com.igs.ematf.dataresttest.controller;


import com.igs.ematf.dataresttest.model.WikiDocument;
import com.igs.ematf.dataresttest.repository.ContentRepository;
import com.igs.ematf.dataresttest.service.impl.OpenSearchHealthIndicator;
import com.igs.ematf.dataresttest.service.impl.OpenSearchServiceImplOfficial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/custom/search")

@Slf4j
@Validated
public class SearchController {

    private final OpenSearchServiceImplOfficial openSearchService;
    private final ContentRepository contentRepository;
    private final OpenSearchHealthIndicator healthIndicator;
    @Value("${opensearch.index}")
    private String wikiIndex;

    public SearchController(OpenSearchServiceImplOfficial openSearchService, ContentRepository contentRepository,
                            OpenSearchHealthIndicator healthIndicator) {
        this.openSearchService = openSearchService;
        this.contentRepository = contentRepository;
        this.healthIndicator = healthIndicator;
    }

    @GetMapping("")
    public Page<WikiDocument> search(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size
    ) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<WikiDocument> docs = openSearchService.search(
                query,
                pageable.getPageNumber(),
                pageable.getPageSize());
        return new PageImpl<>(docs, pageable, docs.size());
    }

    @GetMapping("/health")
    public Health health() {
        return healthIndicator.health();
    }
}
