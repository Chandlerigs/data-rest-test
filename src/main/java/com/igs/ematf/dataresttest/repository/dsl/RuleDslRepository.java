package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.QSysRule;
import com.igs.ematf.dataresttest.entity.SysRule;
import com.querydsl.core.BooleanBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "rule")
@Tag(name = "Rule")
public interface RuleDslRepository extends BaseDslRepository<SysRule, Long, QSysRule> {

    @Override
    default void customize(QuerydslBindings bindings, QSysRule root) {
        /*
         * 当前端参数不传递 【ruleName】， 下面这条筛选不生效
         */
        String filterStr = "AAa";
        bindings.bind(root.ruleName)
                .first(
                        (path, values) -> path.eq(filterStr)
                );

        /*
         * 希望添加默认条件，即使前端不传递参数【ruleName】，默认条件生效
         * 但是 这个无效
         */
        // BooleanBuilder defaultConditions = new BooleanBuilder();
        // defaultConditions.and(root.ruleName.containsIgnoreCase("AAa"));

        /*
         * 绑定所有 String 类型的字段
         */
        bindings.bind(String.class).all((path, value) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            value.forEach(v ->
                    predicate.or(root.ruleName.containsIgnoreCase(filterStr))
                            .or(root.conditionSql.containsIgnoreCase(filterStr))
            );
            return Optional.of(predicate);
        });
    }
}