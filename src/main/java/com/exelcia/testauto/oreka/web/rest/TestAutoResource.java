package com.exelcia.testauto.oreka.web.rest;

import com.exelcia.testauto.oreka.domain.TestAuto;
import com.exelcia.testauto.oreka.repository.TestAutoRepository;
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
 * REST controller for managing {@link com.exelcia.testauto.oreka.domain.TestAuto}.
 */
@RestController
@RequestMapping("/api/test-autos")
@Transactional
public class TestAutoResource {

    private final Logger log = LoggerFactory.getLogger(TestAutoResource.class);

    private static final String ENTITY_NAME = "testAuto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestAutoRepository testAutoRepository;

    public TestAutoResource(TestAutoRepository testAutoRepository) {
        this.testAutoRepository = testAutoRepository;
    }

    /**
     * {@code POST  /test-autos} : Create a new testAuto.
     *
     * @param testAuto the testAuto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testAuto, or with status {@code 400 (Bad Request)} if the testAuto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestAuto> createTestAuto(@Valid @RequestBody TestAuto testAuto) throws URISyntaxException {
        log.debug("REST request to save TestAuto : {}", testAuto);
        if (testAuto.getId() != null) {
            throw new BadRequestAlertException("A new testAuto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestAuto result = testAutoRepository.save(testAuto);
        return ResponseEntity
            .created(new URI("/api/test-autos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-autos/:id} : Updates an existing testAuto.
     *
     * @param id the id of the testAuto to save.
     * @param testAuto the testAuto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testAuto,
     * or with status {@code 400 (Bad Request)} if the testAuto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testAuto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestAuto> updateTestAuto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestAuto testAuto
    ) throws URISyntaxException {
        log.debug("REST request to update TestAuto : {}, {}", id, testAuto);
        if (testAuto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testAuto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testAutoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestAuto result = testAutoRepository.save(testAuto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testAuto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-autos/:id} : Partial updates given fields of an existing testAuto, field will ignore if it is null
     *
     * @param id the id of the testAuto to save.
     * @param testAuto the testAuto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testAuto,
     * or with status {@code 400 (Bad Request)} if the testAuto is not valid,
     * or with status {@code 404 (Not Found)} if the testAuto is not found,
     * or with status {@code 500 (Internal Server Error)} if the testAuto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestAuto> partialUpdateTestAuto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestAuto testAuto
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestAuto partially : {}, {}", id, testAuto);
        if (testAuto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testAuto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testAutoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestAuto> result = testAutoRepository
            .findById(testAuto.getId())
            .map(existingTestAuto -> {
                if (testAuto.getNom() != null) {
                    existingTestAuto.setNom(testAuto.getNom());
                }
                if (testAuto.getStatus() != null) {
                    existingTestAuto.setStatus(testAuto.getStatus());
                }
                if (testAuto.getCategorie() != null) {
                    existingTestAuto.setCategorie(testAuto.getCategorie());
                }
                if (testAuto.getInfos() != null) {
                    existingTestAuto.setInfos(testAuto.getInfos());
                }

                return existingTestAuto;
            })
            .map(testAutoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testAuto.getId().toString())
        );
    }

    /**
     * {@code GET  /test-autos} : get all the testAutos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testAutos in body.
     */
    @GetMapping("")
    public List<TestAuto> getAllTestAutos(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all TestAutos");
        if (eagerload) {
            return testAutoRepository.findAllWithEagerRelationships();
        } else {
            return testAutoRepository.findAll();
        }
    }

    /**
     * {@code GET  /test-autos/:id} : get the "id" testAuto.
     *
     * @param id the id of the testAuto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testAuto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestAuto> getTestAuto(@PathVariable("id") Long id) {
        log.debug("REST request to get TestAuto : {}", id);
        Optional<TestAuto> testAuto = testAutoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(testAuto);
    }

    /**
     * {@code DELETE  /test-autos/:id} : delete the "id" testAuto.
     *
     * @param id the id of the testAuto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestAuto(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestAuto : {}", id);
        testAutoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
