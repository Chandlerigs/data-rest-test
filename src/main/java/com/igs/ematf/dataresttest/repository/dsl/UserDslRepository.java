package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.dto.PermissionCondition;
import com.igs.ematf.dataresttest.entity.QSysUser;
import com.igs.ematf.dataresttest.entity.SysUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


@RepositoryRestResource(path = "user")
@Tag(name = "User")
public interface UserDslRepository extends BaseDslRepository<SysUser, Long, QSysUser> {

    default void customize(QuerydslBindings bindings, QSysUser root) {
        List<String> fieldNames = List.of("username", "age", "department.depName");
        List<PermissionCondition> fieldConditions = List.of(
//                new PermissionCondition("username", "eq", "1", String.class),
                new PermissionCondition("age", "gt", "1", Integer.class),
                new PermissionCondition("department.depName", "eq", "1", String.class)
        );

        Field[] declaredFields = root.getType().getDeclaredFields();
        Arrays.asList(declaredFields).forEach(field -> {
            Path<?> fieldWithRelationSafely = getFieldWithRelationSafely(root, Arrays.asList(field.getName()));
            bindings.excluding(fieldWithRelationSafely); // 嵌套字段 移除 可能需要很多
        });
        bindings.including(root.age); //
        bindings.bind(root.age)
                .first((path, value) -> {
                    BooleanBuilder predicate = new BooleanBuilder();
//                    for (PermissionCondition condition : fieldConditions) {
//                        String fieldName = condition.getFieldName();
//                        Class<?> fieldDataType = condition.getComparisonDataType();
//                        Path fieldPath = ExpressionUtils.path(root.getClass(), fieldName);
//                        Predicate predicateItem = null;
//                        if (fieldDataType.getSuperclass() == Number.class) {
//                            NumberPath numberPath2 = (NumberPath) Expressions.asNumber(fieldPath);
//                            predicateItem = numberPath2.eq(4);
//                            predicate.and(predicateItem);
//                        } else if (fieldDataType == String.class) {
//                            StringPath numberPath2 = (StringPath) Expressions.asString(fieldPath);
//                            predicateItem = numberPath2.eq("Lai Wing Fat");
//                            predicate.or(predicateItem);
//                        } else {
//                            predicateItem = new BooleanBuilder();
//                            predicate.and(predicateItem);
//                        }
//                    }
                    Path ageField = ExpressionUtils.path(root.getClass(), "age");
                    NumberPath agePath = (NumberPath) Expressions.asNumber(ageField);
                    Path depNameField = ExpressionUtils.path(root.getClass(), "department.depName");
                    StringPath depNamePath = (StringPath) Expressions.asString(depNameField);

                    Path usernameField = ExpressionUtils.path(root.getClass(), "username");
                    StringPath usernamePath = (StringPath) Expressions.asString(usernameField);

//                    BooleanExpression ageEq1 = agePath.eq(4);
//                    BooleanExpression depEq1 = depNamePath.eq("dep1");
//                    BooleanExpression ageEq2 = agePath.eq(125);
                    // age = 128 or (age = 4 and depName = 'dep1')
//                    return ageEq2.or(depEq1.and(ageEq1));

//                     age = 4 and (username = 'Nomura Rin' or depName = 'dep1')
                    BooleanExpression ageEq1 = agePath.eq(4);
                    BooleanExpression depEq1 = depNamePath.eq("dep1");
                    BooleanExpression nameEq1 = usernamePath.eq("Nomura Rin");

                    return ageEq1.and(depEq1.or(nameEq1));
                });

    }
}