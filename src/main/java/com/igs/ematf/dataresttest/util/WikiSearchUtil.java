package com.igs.ematf.dataresttest.util;


import com.igs.ematf.dataresttest.entity.Content;
import com.igs.ematf.dataresttest.model.SearchTypeEnum;
import com.igs.ematf.dataresttest.model.WikiDocument;
import org.jsoup.Jsoup;

import java.time.format.DateTimeFormatter;

public class WikiSearchUtil {
    public static WikiDocument convertToDocument(Content content) {
        WikiDocument doc = new WikiDocument();
        doc.setId(content.getId().toString());
        doc.setTitle(content.getTitle());
        doc.setType(SearchTypeEnum.valueOf(content.getContentType().name()));
        // 移除html的 标签
        doc.setContent(Jsoup.parse(content.getContentBody()).text());
        doc.setAuthor(content.getCreatedBy());
        doc.setCreateTime(content.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return doc;
    }


}
