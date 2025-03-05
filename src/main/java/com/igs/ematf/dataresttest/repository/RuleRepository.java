package com.igs.ematf.dataresttest.repository;

import com.igs.ematf.dataresttest.entity.SysRule;
import com.igs.ematf.dataresttest.projection.SysRuleProjection;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "rule", path = "rule", excerptProjection = SysRuleProjection.class)
@Tag(name = "SysRule", description = "SysRule Api")
public interface RuleRepository extends BaseRepository<SysRule, Long> {
}
