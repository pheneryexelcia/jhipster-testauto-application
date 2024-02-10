package com.exelcia.testauto.oreka.repository;

import com.exelcia.testauto.oreka.domain.TestAuto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestAuto entity.
 */
@Repository
public interface TestAutoRepository extends JpaRepository<TestAuto, Long> {
    default Optional<TestAuto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TestAuto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TestAuto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select testAuto from TestAuto testAuto left join fetch testAuto.rapport",
        countQuery = "select count(testAuto) from TestAuto testAuto"
    )
    Page<TestAuto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select testAuto from TestAuto testAuto left join fetch testAuto.rapport")
    List<TestAuto> findAllWithToOneRelationships();

    @Query("select testAuto from TestAuto testAuto left join fetch testAuto.rapport where testAuto.id =:id")
    Optional<TestAuto> findOneWithToOneRelationships(@Param("id") Long id);
}
