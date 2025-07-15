package com.igs.ematf.dataresttest.model;

import lombok.Data;

@Data
public class WikiDocument {
    private String id;
    private SearchTypeEnum type;
    private String title;
    private String titleHighlight;
    private String content;
    private String contentHighlight;
    private String author;
    private String createTime;
}