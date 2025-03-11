package com.igs.ematf.dataresttest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_rule")
@EntityListeners({AuditingEntityListener.class})
@ToString
public class SysRule implements Serializable {
    @Id
    @Column(name = "id", length = 18)
    @JsonProperty("id")
    private Long id;

    private String ruleName;

    private String conditionSql;

}
