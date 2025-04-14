package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.QSysUser;
import com.igs.ematf.dataresttest.entity.SysUser;
import com.querydsl.core.types.dsl.StringExpression;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "user")
@Tag(name = "User")
public interface UserDslRepository extends BaseDslRepository<SysUser, Long, QSysUser> {
    default void customize(QuerydslBindings bindings, QSysUser root) {
        bindings.bind(root.username).first(StringExpression::contains);
    }
}