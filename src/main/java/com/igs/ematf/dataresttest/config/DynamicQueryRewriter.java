package com.igs.ematf.dataresttest.config;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryRewriter;

public class DynamicQueryRewriter implements QueryRewriter {
    @Override
    public String rewrite(String query, Sort sort) {
        return query.replaceAll("1 = 1", " 2 = 2 ");
    }
}
