package com.igs.ematf.dataresttest.repository;

import com.igs.ematf.dataresttest.config.DynamicQueryRewriter;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    @Query(value = "select u from #{#entityName} u where 1 = 1",
            queryRewriter = DynamicQueryRewriter.class)
    Page<T> findByParams(@Nullable @Param("params") String params, @Nullable Pageable pageable);

}
