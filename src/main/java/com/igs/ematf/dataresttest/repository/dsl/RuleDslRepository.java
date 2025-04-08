package com.igs.ematf.dataresttest.repository.dsl;

import com.igs.ematf.dataresttest.entity.QSysRule;
import com.igs.ematf.dataresttest.entity.SysRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "Rule", path = "Rule")
@Tag(name = "Rule", description = "Rule Api")
public interface RuleDslRepository extends BaseDslRepository<SysRule, Long, QSysRule> {
}