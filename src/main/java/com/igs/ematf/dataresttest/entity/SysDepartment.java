package com.igs.ematf.dataresttest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_dep")
@EntityListeners({AuditingEntityListener.class})
public class SysDepartment extends BaseEntity {
    private String depName;
    private String description;
}
