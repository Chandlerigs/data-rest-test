package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.dto.PermissionCondition;
import com.igs.ematf.dataresttest.entity.SysDepartment;
import com.igs.ematf.dataresttest.entity.SysUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
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
        List<PermissionCondition> permissions = getPermissions(new SysUser(), root);
        for (PermissionCondition permission : permissions) {
            String fieldName = permission.getFieldName();
            String comparisonOperator = permission.getComparisonOperator();
            String fieldValue = permission.getComparisonData();
            Path<?> fieldSafely = getFieldSafely(root, fieldName);
            if (fieldSafely == null) {
                continue;
            }
            // 添加 行权限 AND 条件
            bindings.bind(fieldSafely).first((path, value) -> getPredicate(path, comparisonOperator, fieldValue));
        }

    }

    /**
     * 模拟行权限
     * @param currentUser 当前用户信息
     * @param root 当前Q类
     * @return 行权限列表
     */
    private List<PermissionCondition> getPermissions(SysUser currentUser, Q root) {
        // 获取实体类名
        Class<?> type = root.getType();
        // 通过上下文获取用户信息
        // currentUser
        // 根据用户信息和当前访问实体类，获取用户行权限信息
        List<PermissionCondition> permissions = new ArrayList<>();
        if (type == SysDepartment.class) {
            permissions.add(new PermissionCondition("id", ">=", "20"));
            permissions.add(new PermissionCondition("description", "eq", "test description 111"));
        }
        return permissions;

    }
    private Path<?> getFieldSafely(Q root, String... possibleFieldNames) {
        for (String fieldName : possibleFieldNames) {
            try {
                Field field = root.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return (Path<?>) field.get(root);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("QueryDSL 获取字段类型失败, " +
                        "类型：" + root.getClass() +
                        "字段名称：" + Arrays.toString(possibleFieldNames) +
                        "，错误信息：" + e.getMessage());
            }
        }
        return null;
    }
}