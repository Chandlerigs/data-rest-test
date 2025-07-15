package com.igs.ematf.dataresttest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Data
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "snowFlakeIdGenerator")
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "com.igsl.wiki.common.orm.config.SnowFlakeIdGenerator")
    @Column(name = "id", length = 18)
    @JsonProperty("id")
    private Long id;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Nullable
    private LocalDateTime createdTime;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    @Nullable
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Nullable
    private LocalDateTime updatedTime;

    @LastModifiedBy
    @Column(name = "updated_by")
    @Nullable
    private String updatedBy;

    @Transient
    private Long preId;

    /**
     * 该字段仅供执行QueryDSL自定义查询时，存储查询sql参数，不存储到数据库中
     */
    private String filter;
}
