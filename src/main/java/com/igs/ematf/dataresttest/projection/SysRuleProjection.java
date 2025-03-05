package com.igs.ematf.dataresttest.projection;

import com.igs.ematf.dataresttest.entity.SysRule;
import org.springframework.data.rest.core.config.Projection;


@Projection(name = "ruleProjection", types = {SysRule.class})
public interface SysRuleProjection {
    Long getId();

    String getRuleName();
}
