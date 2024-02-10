package com.exelcia.testauto.oreka.repository;

import com.exelcia.testauto.oreka.domain.EtapeTest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EtapeTest entity.
 */
@Repository
public interface EtapeTestRepository extends JpaRepository<EtapeTest, Long> {
    default Optional<EtapeTest> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EtapeTest> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EtapeTest> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select etapeTest from EtapeTest etapeTest left join fetch etapeTest.testAuto",
        countQuery = "select count(etapeTest) from EtapeTest etapeTest"
    )
    Page<EtapeTest> findAllWithToOneRelationships(Pageable pageable);

    @Query("select etapeTest from EtapeTest etapeTest left join fetch etapeTest.testAuto")
    List<EtapeTest> findAllWithToOneRelationships();

    @Query("select etapeTest from EtapeTest etapeTest left join fetch etapeTest.testAuto where etapeTest.id =:id")
    Optional<EtapeTest> findOneWithToOneRelationships(@Param("id") Long id);
}
