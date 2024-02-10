package com.exelcia.testauto.oreka.web.rest;

import com.exelcia.testauto.oreka.domain.Rapport;
import com.exelcia.testauto.oreka.repository.RapportRepository;
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
 * REST controller for managing {@link com.exelcia.testauto.oreka.domain.Rapport}.
 */
@RestController
@RequestMapping("/api/rapports")
@Transactional
public class RapportResource {

    private final Logger log = LoggerFactory.getLogger(RapportResource.class);

    private static final String ENTITY_NAME = "rapport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RapportRepository rapportRepository;

    public RapportResource(RapportRepository rapportRepository) {
        this.rapportRepository = rapportRepository;
    }

    /**
     * {@code POST  /rapports} : Create a new rapport.
     *
     * @param rapport the rapport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rapport, or with status {@code 400 (Bad Request)} if the rapport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Rapport> createRapport(@Valid @RequestBody Rapport rapport) throws URISyntaxException {
        log.debug("REST request to save Rapport : {}", rapport);
        if (rapport.getId() != null) {
            throw new BadRequestAlertException("A new rapport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rapport result = rapportRepository.save(rapport);
        return ResponseEntity
            .created(new URI("/api/rapports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rapports/:id} : Updates an existing rapport.
     *
     * @param id the id of the rapport to save.
     * @param rapport the rapport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapport,
     * or with status {@code 400 (Bad Request)} if the rapport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rapport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rapport> updateRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Rapport rapport
    ) throws URISyntaxException {
        log.debug("REST request to update Rapport : {}, {}", id, rapport);
        if (rapport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rapport result = rapportRepository.save(rapport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rapports/:id} : Partial updates given fields of an existing rapport, field will ignore if it is null
     *
     * @param id the id of the rapport to save.
     * @param rapport the rapport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapport,
     * or with status {@code 400 (Bad Request)} if the rapport is not valid,
     * or with status {@code 404 (Not Found)} if the rapport is not found,
     * or with status {@code 500 (Internal Server Error)} if the rapport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rapport> partialUpdateRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rapport rapport
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rapport partially : {}, {}", id, rapport);
        if (rapport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rapport> result = rapportRepository
            .findById(rapport.getId())
            .map(existingRapport -> {
                if (rapport.getNom() != null) {
                    existingRapport.setNom(rapport.getNom());
                }
                if (rapport.getDateStr() != null) {
                    existingRapport.setDateStr(rapport.getDateStr());
                }
                if (rapport.getEnvironnement() != null) {
                    existingRapport.setEnvironnement(rapport.getEnvironnement());
                }
                if (rapport.getNbtests() != null) {
                    existingRapport.setNbtests(rapport.getNbtests());
                }
                if (rapport.getNbtestsOk() != null) {
                    existingRapport.setNbtestsOk(rapport.getNbtestsOk());
                }
                if (rapport.getNbtestsKo() != null) {
                    existingRapport.setNbtestsKo(rapport.getNbtestsKo());
                }
                if (rapport.getLogo() != null) {
                    existingRapport.setLogo(rapport.getLogo());
                }

                return existingRapport;
            })
            .map(rapportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapport.getId().toString())
        );
    }

    /**
     * {@code GET  /rapports} : get all the rapports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapports in body.
     */
    @GetMapping("")
    public List<Rapport> getAllRapports() {
        log.debug("REST request to get all Rapports");
        return rapportRepository.findAll();
    }

    /**
     * {@code GET  /rapports/:id} : get the "id" rapport.
     *
     * @param id the id of the rapport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rapport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rapport> getRapport(@PathVariable("id") Long id) {
        log.debug("REST request to get Rapport : {}", id);
        Optional<Rapport> rapport = rapportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rapport);
    }

    /**
     * {@code DELETE  /rapports/:id} : delete the "id" rapport.
     *
     * @param id the id of the rapport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRapport(@PathVariable("id") Long id) {
        log.debug("REST request to delete Rapport : {}", id);
        rapportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
