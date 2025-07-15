package com.igs.ematf.dataresttest.repository;

import com.igs.ematf.dataresttest.entity.Content;
import com.igs.ematf.dataresttest.projection.ContentProjection;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * content Repository
 *
 * @author Samuel
 * @since 2025/4/10 14:27
 */
@Tag(name = "Content")
@RepositoryRestResource(collectionResourceRel = "content", path = "content", excerptProjection =
        ContentProjection.class)
public interface ContentRepository extends BaseRepository<Content, Long> {

}
