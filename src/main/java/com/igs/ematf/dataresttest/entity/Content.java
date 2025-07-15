package com.igs.ematf.dataresttest.entity;

import com.igs.ematf.dataresttest.model.ContentTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面、博文、评论、附件等
 *
 * @author Samuel
 * @version 1.0
 * @since 2025/4/21 10:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content")
public class Content extends BaseEntity {
    private String title; // 标题
    private Long version; // 版本号

    /**
     * 是否为更新未发布
     */
    private Boolean unpublished;

    @Column(columnDefinition = "TEXT")
    private String contentBody;

    @Column(columnDefinition = "TEXT")
    private String versionComment; // 当前版本的注释
    @Enumerated(EnumType.STRING)
    private ContentTypeEnum contentType; // 内容的类型--页面、博文、评论、、、

}
