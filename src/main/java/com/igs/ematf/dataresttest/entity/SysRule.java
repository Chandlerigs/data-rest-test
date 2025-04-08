package com.igs.ematf.dataresttest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_rule")
@EntityListeners({AuditingEntityListener.class})
public class SysRule extends BaseEntity {
    @Id
    @Column(name = "id", length = 18)
    @JsonProperty("id")
    private Long id;

    private String ruleName;

    private String conditionSql;

}
