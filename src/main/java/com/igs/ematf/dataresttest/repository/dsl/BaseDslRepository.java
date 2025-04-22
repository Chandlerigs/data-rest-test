package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.dto.PermissionCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.StringUtils;

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
                return numberPath.eq(fieldValueFormat);
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
//        List<PermissionCondition> permissions = getPermissions(root);
//        List<Path> paths = new ArrayList<>();
//        for (PermissionCondition permission : permissions) {
//            String tableName = permission.getTableName();
//            String fieldName = permission.getFieldName();
//            List<String> fieldNameList = new ArrayList<>(Arrays.asList(tableName.split("\\.")));
//            fieldNameList.add(fieldName);
//            List<String> list = fieldNameList.stream().filter(StringUtils::hasLength).toList();
//            Path<?> fieldSafely = getFieldWithRelationSafely(root, list);
//            if (fieldSafely != null) {
//                paths.add(fieldSafely);
//            }
//        }

        // 绑定多个字段的复合条件
//        bindings.bind((Path[]) paths.toArray()).first((path, value) -> {
//            return ((NumberPath) path).goe((Number) value);
//        });
//        bindings.bind(Object.class).first((path, value) -> {
//            BooleanBuilder predicate = new BooleanBuilder();
//            Path path1 = paths.get(0);
//            if (path1 instanceof StringPath path1StringPath) {
//                // 如果字段是 StringPath，则使用 containsIgnoreCase 方法
//                predicate.and(path1StringPath.containsIgnoreCase(value.toString()));
//            } else if (path1 instanceof NumberPath path1StringPath) {
//                // 如果字段是 StringPath，则使用 containsIgnoreCase 方法
//                predicate.and(path1StringPath.gt((Number) value));
//            } else {
//                return new BooleanBuilder();
//            }
//
//
//            // 获取字段名
//            String fieldName = path.getMetadata().getName();
//            Path<Object> path2 = ExpressionUtils.path(Object.class, fieldName);
//
//            // 根据字段名判断类型并构造条件
////            if ("name".equals(fieldName)) {
////                // name 是 String 类型字段
////
////
////                path1.getMetadata().getName();
////                predicate.and(root.name.equalsIgnoreCase(value.toString()));
////            } else if ("id".equals(fieldName)) {
////                // id 是 Number 类型字段
////                predicate.and(root.id.eq(Long.valueOf(value.toString())));
////            }
//
//            return predicate;
//        });
    }


    /**
     * 模拟行权限
     * @param root 当前Q类
     * @return 行权限列表
     */
    private List<PermissionCondition> getPermissions(Q root) {
        // 根据用户信息和当前访问实体类，获取用户行权限信息
        List<PermissionCondition> permissions = new ArrayList<>();
        PermissionCondition condition = getPermissionCondition();
        permissions.add(condition);
        PermissionCondition condition2 = getPermissionCondition2();
        permissions.add(condition2);
        return permissions;
    }

    private PermissionCondition getPermissionCondition() {
        PermissionCondition condition = new PermissionCondition();
        condition.setTableName("department.rule");
        condition.setFieldName("ruleName");
        condition.setComparisonData("bbb");
        condition.setComparisonOperator("=");
        condition.setComparisonDataType(String.class);
        return condition;
    }

    private PermissionCondition getPermissionCondition2() {
        PermissionCondition condition = new PermissionCondition();
        condition.setTableName("");
        condition.setFieldName("username");
        condition.setComparisonData("1");
        condition.setComparisonOperator("=");
        condition.setComparisonDataType(Long.class);
        return condition;
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

    default Path<?> getFieldWithRelationSafely(Object root, List<String> fieldNameList) {
        try {
            for (int i = 0; i < fieldNameList.size(); i++) {
                String fieldItemName = fieldNameList.get(i);
                Field field = root.getClass().getDeclaredField(fieldItemName);
                field.setAccessible(true);
                Object rootField = field.get(root);
                if (i == fieldNameList.size() - 1) {
//                    return ExpressionUtils.path(Object.class, fieldNameList.get(i));
                    if (field.getType().getSuperclass() == EntityPathBase.class) {
                        // 如果字段是 EntityPathBase 的子类，则返回其 id 字段
                        Field idField = rootField.getClass().getDeclaredField("id"); // 获取 id 字段
                        idField.setAccessible(true); // 确保可以访问私有字段
                        return (Path<?>) idField.get(rootField); // 返回 id 字段
                    }
                }
                root = rootField;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Path<?>) root;
    }
}