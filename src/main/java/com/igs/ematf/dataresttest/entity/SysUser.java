package com.igs.ematf.dataresttest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sys_user")
@EntityListeners({AuditingEntityListener.class})
public class SysUser extends BaseEntity {
    private String username;
    private Integer age;

    @ManyToOne
    private SysDepartment department;
}
