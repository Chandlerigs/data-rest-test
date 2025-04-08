package com.igs.ematf.dataresttest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Data
public class BaseEntity implements Serializable {
    @Id
    @Column(name = "id", length = 18)
    @JsonProperty("id")
    private Long id;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Nullable
    private LocalDateTime createdTime;

//    @CreatedBy
//    @Column(name = "created_by", updatable = false)
//    @Nullable
//    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Nullable
    private LocalDateTime updatedTime;

//    @LastModifiedBy
//    @Column(name = "updated_by")
//    @Nullable
//    private String updatedBy;

}
