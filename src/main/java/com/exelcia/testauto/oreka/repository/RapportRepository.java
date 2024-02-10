package com.exelcia.testauto.oreka.repository;

import com.exelcia.testauto.oreka.domain.Rapport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rapport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {}
