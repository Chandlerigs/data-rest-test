package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.QSysDepartment;
import com.igs.ematf.dataresttest.entity.SysDepartment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RepositoryRestResource(path = "department")
@Tag(name = "Department")
public interface DepartmentDslRepository extends BaseDslRepository<SysDepartment, Long, QSysDepartment> {
//    @Override
//    default void customize(QuerydslBindings bindings, QSysDepartment root) {
//        List<Path> allPath = new ArrayList<>();
//        for (Field field : root.getType().getDeclaredFields()) {
//
//            Path<?> fieldWithRelationSafely = getFieldWithRelationSafely(root, Arrays.asList(field.getName()));
//            allPath.add(fieldWithRelationSafely);
//
//        }
//        Path[] array = (Path[]) allPath.toArray();
//
//        bindings.bind(root.description, root.depName, root.id).first((path, value) -> {
//            BooleanBuilder predicate = new BooleanBuilder();
//
//            if (fieldDataType.getSuperclass() == Number.class) {
//                NumberPath numberPath2 = (NumberPath) Expressions.asNumber(fieldPath);
//                predicateItem = numberPath2.eq(4);
//                predicate.and(predicateItem);
//            } else if (fieldDataType == String.class) {
//                StringPath numberPath2 = (StringPath) Expressions.asString(fieldPath);
//                predicateItem = numberPath2.eq("Lai Wing Fat");
//                predicate.or(predicateItem);
//            } else {
//                predicateItem = new BooleanBuilder();
//                predicate.and(predicateItem);
//            }
//
//            return predicate;
//        });
//    }
}