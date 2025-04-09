package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.SysUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Field;

@NoRepositoryBean
public interface BaseDslRepository<T, ID, Q extends EntityPath<?>> extends JpaRepository<T, ID>,
        QuerydslPredicateExecutor<T>,
        QuerydslBinderCustomizer<Q> {

    // 自定义复杂查询
    @Override
    default void customize(QuerydslBindings bindings, Q root) {

//        bindings.bind(String.class)
//                .first((StringPath path, String value) -> path.containsIgnoreCase(value));
//        bindings.bind(Long.class)
//                .first((NumberPath<Long> path, Long value) -> path.goe(value));
        // 获取实体类名
        Class<?> type = root.getType();
        // 通过上下文获取用户信息
        SysUser currentUser = new SysUser();
        // 根据用户信息和当前访问实体类，获取用户行权限信息 sql
        String rowPermissionSql = "id > 10";

        // 解析行权限sql。转化为binding
        String fieldName = "description";
        String fieldValue = "test description 111";
        Path<?> fieldSafely = getFieldSafely(root, fieldName);
        if (fieldSafely != null) {
            Class<?> fieldClazz = fieldSafely.getType();
            // 添加 行权限 AND 条件
            if (fieldClazz == String.class) {
                bindings.bind(fieldSafely).first((path, value) -> ((StringPath) path).eq(fieldValue));
            } else if (fieldClazz == Long.class) {
                bindings.bind(fieldSafely).first((path, value) -> {
                    if (path instanceof NumberPath<?> numberPath) {
                        return numberPath.goe((Long) value);
                    } else {
                        return new BooleanBuilder();
                    }
                });
            } else {
                // 其他类型，根据需要添加相应的条件
            }
        }
    }

    private Path<?> getFieldSafely(Q root, String... possibleFieldNames) {
        for (String fieldName : possibleFieldNames) {
            try {
                Field field = root.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return (Path<?>) field.get(root);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 继续尝试下一个可能的字段名
            }
        }
        return null;
    }
}