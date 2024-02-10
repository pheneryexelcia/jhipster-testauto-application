package com.exelcia.testauto.oreka.web.rest;

import com.exelcia.testauto.oreka.domain.EtapeTest;
import com.exelcia.testauto.oreka.repository.EtapeTestRepository;
import com.exelcia.testauto.oreka.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.exelcia.testauto.oreka.domain.EtapeTest}.
 */
@RestController
@RequestMapping("/api/etape-tests")
@Transactional
public class EtapeTestResource {

    private final Logger log = LoggerFactory.getLogger(EtapeTestResource.class);

    private static final String ENTITY_NAME = "etapeTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtapeTestRepository etapeTestRepository;

    public EtapeTestResource(EtapeTestRepository etapeTestRepository) {
        this.etapeTestRepository = etapeTestRepository;
    }

    /**
     * {@code POST  /etape-tests} : Create a new etapeTest.
     *
     * @param etapeTest the etapeTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etapeTest, or with status {@code 400 (Bad Request)} if the etapeTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EtapeTest> createEtapeTest(@Valid @RequestBody EtapeTest etapeTest) throws URISyntaxException {
        log.debug("REST request to save EtapeTest : {}", etapeTest);
        if (etapeTest.getId() != null) {
            throw new BadRequestAlertException("A new etapeTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtapeTest result = etapeTestRepository.save(etapeTest);
        return ResponseEntity
            .created(new URI("/api/etape-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etape-tests/:id} : Updates an existing etapeTest.
     *
     * @param id the id of the etapeTest to save.
     * @param etapeTest the etapeTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapeTest,
     * or with status {@code 400 (Bad Request)} if the etapeTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etapeTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EtapeTest> updateEtapeTest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EtapeTest etapeTest
    ) throws URISyntaxException {
        log.debug("REST request to update EtapeTest : {}, {}", id, etapeTest);
        if (etapeTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapeTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etapeTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EtapeTest result = etapeTestRepository.save(etapeTest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etapeTest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etape-tests/:id} : Partial updates given fields of an existing etapeTest, field will ignore if it is null
     *
     * @param id the id of the etapeTest to save.
     * @param etapeTest the etapeTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etapeTest,
     * or with status {@code 400 (Bad Request)} if the etapeTest is not valid,
     * or with status {@code 404 (Not Found)} if the etapeTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the etapeTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EtapeTest> partialUpdateEtapeTest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EtapeTest etapeTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update EtapeTest partially : {}, {}", id, etapeTest);
        if (etapeTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etapeTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etapeTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EtapeTest> result = etapeTestRepository
            .findById(etapeTest.getId())
            .map(existingEtapeTest -> {
                if (etapeTest.getNom() != null) {
                    existingEtapeTest.setNom(etapeTest.getNom());
                }
                if (etapeTest.getStatus() != null) {
                    existingEtapeTest.setStatus(etapeTest.getStatus());
                }
                if (etapeTest.getInfos() != null) {
                    existingEtapeTest.setInfos(etapeTest.getInfos());
                }

                return existingEtapeTest;
            })
            .map(etapeTestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etapeTest.getId().toString())
        );
    }

    /**
     * {@code GET  /etape-tests} : get all the etapeTests.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etapeTests in body.
     */
    @GetMapping("")
    public List<EtapeTest> getAllEtapeTests(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all EtapeTests");
        if (eagerload) {
            return etapeTestRepository.findAllWithEagerRelationships();
        } else {
            return etapeTestRepository.findAll();
        }
    }

    /**
     * {@code GET  /etape-tests/:id} : get the "id" etapeTest.
     *
     * @param id the id of the etapeTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etapeTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EtapeTest> getEtapeTest(@PathVariable("id") Long id) {
        log.debug("REST request to get EtapeTest : {}", id);
        Optional<EtapeTest> etapeTest = etapeTestRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(etapeTest);
    }

    /**
     * {@code DELETE  /etape-tests/:id} : delete the "id" etapeTest.
     *
     * @param id the id of the etapeTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtapeTest(@PathVariable("id") Long id) {
        log.debug("REST request to delete EtapeTest : {}", id);
        etapeTestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
