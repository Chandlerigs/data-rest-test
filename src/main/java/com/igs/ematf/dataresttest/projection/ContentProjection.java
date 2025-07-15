package com.igs.ematf.dataresttest.projection;


import com.igs.ematf.dataresttest.entity.Content;
import com.igs.ematf.dataresttest.model.ContentTypeEnum;
import org.springframework.data.rest.core.config.Projection;

/**
 * content default projection
 *
 * @author Samuel
 * @since 2025/4/10 14:19
 */
@Projection(name = "contentProjection", types = {Content.class})
public interface ContentProjection {

    Long getId();

    String getTitle();

    Long getVersion();

    ContentTypeEnum getContentType();


    String getContentBody();

    String getVersionComment();


}
