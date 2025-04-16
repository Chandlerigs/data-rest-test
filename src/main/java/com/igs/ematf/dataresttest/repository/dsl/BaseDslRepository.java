package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.dto.PermissionCondition;
import com.igs.ematf.dataresttest.entity.SysDepartment;
import com.igs.ematf.dataresttest.entity.SysUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoRepositoryBean
public interface BaseDslRepository<T, ID, Q extends EntityPath<?>> extends JpaRepository<T, ID>,
        QuerydslPredicateExecutor<T>,
        QuerydslBinderCustomizer<Q> {

    private static Predicate getPredicate(Path<?> path, String comparisonOperator, Object fieldValue) {
        if (path instanceof StringPath numberPath) {
            String fieldValueFormat = (fieldValue.toString());
            if ("eq".equals(comparisonOperator)) {
                return numberPath.eq(fieldValueFormat);
            } else {
                return numberPath.ne(fieldValueFormat);
            }
        }
        if (path instanceof NumberPath<?> numberPath) {
            long fieldValueFormat = Long.parseLong(fieldValue.toString());
            if (">=".equals(comparisonOperator)) {
                return numberPath.goe(fieldValueFormat);
            } else {
                return numberPath.loe(fieldValueFormat);
            }
        } else {
            return new BooleanBuilder();
        }
    }


    // 自定义复杂查询
    @Override
    default void customize(QuerydslBindings bindings, Q root) {

//        bindings.bind(String.class)
//                .first((StringPath path, String value) -> path.containsIgnoreCase(value));
//        bindings.bind(Long.class)
//                .first((NumberPath<Long> path, Long value) -> path.goe(value));

        // 解析行权限。转化为binding
        List<PermissionCondition> permissions = getPermissions(root);
        for (PermissionCondition permission : permissions) {
            String fieldName = permission.getFieldName();
            String comparisonOperator = permission.getComparisonOperator();
            String fieldValue = permission.getComparisonData();
            Path<?> fieldSafely = getFieldSafely(root, fieldName);
            if (fieldSafely == null) {
                continue;
            }
            // 添加 行权限 AND 条件
            bindings.bind(fieldSafely).first((path, value) -> {
                return getPredicate(path, comparisonOperator, fieldValue);
            });
        }

    }

    /**
     * 模拟行权限
     * @param root 当前Q类
     * @return 行权限列表
     */
    private List<PermissionCondition> getPermissions(Q root) {
        // 根据用户信息和当前访问实体类，获取用户行权限信息
        List<PermissionCondition> permissions = new ArrayList<>();
        permissions.add(new PermissionCondition("department", "gt", "5"));
        return permissions;
    }

    private Path<?> getFieldSafely(Q root, String fieldName) {
        try {
            Field field = root.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object rootField = field.get(root);
            if (field.getType().getSuperclass() == EntityPathBase.class) {
                // 如果字段是 EntityPathBase 的子类，则返回其 id 字段
                Field idField = rootField.getClass().getDeclaredField("id"); // 获取 id 字段
                idField.setAccessible(true); // 确保可以访问私有字段
                return (Path<?>) idField.get(rootField); // 返回 id 字段
            } else {
                return (Path<?>) rootField;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("QueryDSL 获取字段类型失败, " +
                    "类型：" + root.getClass() +
                    "，字段名称：" + fieldName +
                    "，错误信息：" + e.getMessage());
        }
        return null;
    }
}