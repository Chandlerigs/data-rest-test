package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.QSysDepartment;
import com.igs.ematf.dataresttest.entity.SysDepartment;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "Department", path = "Department")
@Tag(name = "Department", description = "Department Api")
public interface DepartmentDslRepository extends BaseDslRepository<SysDepartment, Long, QSysDepartment> {
}