package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.QSysUser;
import com.igs.ematf.dataresttest.entity.SysUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "User", path = "User")
@Tag(name = "User", description = "User Api")
public interface UserDslRepository extends BaseDslRepository<SysUser, Long, QSysUser> {
}