package com.exelcia.testauto.oreka.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exelcia.testauto.oreka.IntegrationTest;
import com.exelcia.testauto.oreka.domain.Rapport;
import com.exelcia.testauto.oreka.repository.RapportRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RapportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapportResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_STR = "AAAAAAAAAA";
    private static final String UPDATED_DATE_STR = "BBBBBBBBBB";

    private static final String DEFAULT_ENVIRONNEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ENVIRONNEMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBTESTS = 1;
    private static final Integer UPDATED_NBTESTS = 2;

    private static final Integer DEFAULT_NBTESTS_OK = 1;
    private static final Integer UPDATED_NBTESTS_OK = 2;

    private static final Integer DEFAULT_NBTESTS_KO = 1;
    private static final Integer UPDATED_NBTESTS_KO = 2;

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rapports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportMockMvc;

    private Rapport rapport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createEntity(EntityManager em) {
        Rapport rapport = new Rapport()
            .nom(DEFAULT_NOM)
            .dateStr(DEFAULT_DATE_STR)
            .environnement(DEFAULT_ENVIRONNEMENT)
            .nbtests(DEFAULT_NBTESTS)
            .nbtestsOk(DEFAULT_NBTESTS_OK)
            .nbtestsKo(DEFAULT_NBTESTS_KO)
            .logo(DEFAULT_LOGO);
        return rapport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createUpdatedEntity(EntityManager em) {
        Rapport rapport = new Rapport()
            .nom(UPDATED_NOM)
            .dateStr(UPDATED_DATE_STR)
            .environnement(UPDATED_ENVIRONNEMENT)
            .nbtests(UPDATED_NBTESTS)
            .nbtestsOk(UPDATED_NBTESTS_OK)
            .nbtestsKo(UPDATED_NBTESTS_KO)
            .logo(UPDATED_LOGO);
        return rapport;
    }

    @BeforeEach
    public void initTest() {
        rapport = createEntity(em);
    }

    @Test
    @Transactional
    void createRapport() throws Exception {
        int databaseSizeBeforeCreate = rapportRepository.findAll().size();
        // Create the Rapport
        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isCreated());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeCreate + 1);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRapport.getDateStr()).isEqualTo(DEFAULT_DATE_STR);
        assertThat(testRapport.getEnvironnement()).isEqualTo(DEFAULT_ENVIRONNEMENT);
        assertThat(testRapport.getNbtests()).isEqualTo(DEFAULT_NBTESTS);
        assertThat(testRapport.getNbtestsOk()).isEqualTo(DEFAULT_NBTESTS_OK);
        assertThat(testRapport.getNbtestsKo()).isEqualTo(DEFAULT_NBTESTS_KO);
        assertThat(testRapport.getLogo()).isEqualTo(DEFAULT_LOGO);
    }

    @Test
    @Transactional
    void createRapportWithExistingId() throws Exception {
        // Create the Rapport with an existing ID
        rapport.setId(1L);

        int databaseSizeBeforeCreate = rapportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportRepository.findAll().size();
        // set the field null
        rapport.setNom(null);

        // Create the Rapport, which fails.

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isBadRequest());

        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateStrIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportRepository.findAll().size();
        // set the field null
        rapport.setDateStr(null);

        // Create the Rapport, which fails.

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isBadRequest());

        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnvironnementIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportRepository.findAll().size();
        // set the field null
        rapport.setEnvironnement(null);

        // Create the Rapport, which fails.

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isBadRequest());

        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRapports() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateStr").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].environnement").value(hasItem(DEFAULT_ENVIRONNEMENT)))
            .andExpect(jsonPath("$.[*].nbtests").value(hasItem(DEFAULT_NBTESTS)))
            .andExpect(jsonPath("$.[*].nbtestsOk").value(hasItem(DEFAULT_NBTESTS_OK)))
            .andExpect(jsonPath("$.[*].nbtestsKo").value(hasItem(DEFAULT_NBTESTS_KO)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getRapport() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        // Get the rapport
        restRapportMockMvc
            .perform(get(ENTITY_API_URL_ID, rapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapport.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateStr").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.environnement").value(DEFAULT_ENVIRONNEMENT))
            .andExpect(jsonPath("$.nbtests").value(DEFAULT_NBTESTS))
            .andExpect(jsonPath("$.nbtestsOk").value(DEFAULT_NBTESTS_OK))
            .andExpect(jsonPath("$.nbtestsKo").value(DEFAULT_NBTESTS_KO))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO));
    }

    @Test
    @Transactional
    void getNonExistingRapport() throws Exception {
        // Get the rapport
        restRapportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRapport() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();

        // Update the rapport
        Rapport updatedRapport = rapportRepository.findById(rapport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRapport are not directly saved in db
        em.detach(updatedRapport);
        updatedRapport
            .nom(UPDATED_NOM)
            .dateStr(UPDATED_DATE_STR)
            .environnement(UPDATED_ENVIRONNEMENT)
            .nbtests(UPDATED_NBTESTS)
            .nbtestsOk(UPDATED_NBTESTS_OK)
            .nbtestsKo(UPDATED_NBTESTS_KO)
            .logo(UPDATED_LOGO);

        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRapport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRapport.getDateStr()).isEqualTo(UPDATED_DATE_STR);
        assertThat(testRapport.getEnvironnement()).isEqualTo(UPDATED_ENVIRONNEMENT);
        assertThat(testRapport.getNbtests()).isEqualTo(UPDATED_NBTESTS);
        assertThat(testRapport.getNbtestsOk()).isEqualTo(UPDATED_NBTESTS_OK);
        assertThat(testRapport.getNbtestsKo()).isEqualTo(UPDATED_NBTESTS_KO);
        assertThat(testRapport.getLogo()).isEqualTo(UPDATED_LOGO);
    }

    @Test
    @Transactional
    void putNonExistingRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport.nom(UPDATED_NOM).dateStr(UPDATED_DATE_STR).nbtestsOk(UPDATED_NBTESTS_OK);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRapport.getDateStr()).isEqualTo(UPDATED_DATE_STR);
        assertThat(testRapport.getEnvironnement()).isEqualTo(DEFAULT_ENVIRONNEMENT);
        assertThat(testRapport.getNbtests()).isEqualTo(DEFAULT_NBTESTS);
        assertThat(testRapport.getNbtestsOk()).isEqualTo(UPDATED_NBTESTS_OK);
        assertThat(testRapport.getNbtestsKo()).isEqualTo(DEFAULT_NBTESTS_KO);
        assertThat(testRapport.getLogo()).isEqualTo(DEFAULT_LOGO);
    }

    @Test
    @Transactional
    void fullUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport
            .nom(UPDATED_NOM)
            .dateStr(UPDATED_DATE_STR)
            .environnement(UPDATED_ENVIRONNEMENT)
            .nbtests(UPDATED_NBTESTS)
            .nbtestsOk(UPDATED_NBTESTS_OK)
            .nbtestsKo(UPDATED_NBTESTS_KO)
            .logo(UPDATED_LOGO);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRapport.getDateStr()).isEqualTo(UPDATED_DATE_STR);
        assertThat(testRapport.getEnvironnement()).isEqualTo(UPDATED_ENVIRONNEMENT);
        assertThat(testRapport.getNbtests()).isEqualTo(UPDATED_NBTESTS);
        assertThat(testRapport.getNbtestsOk()).isEqualTo(UPDATED_NBTESTS_OK);
        assertThat(testRapport.getNbtestsKo()).isEqualTo(UPDATED_NBTESTS_KO);
        assertThat(testRapport.getLogo()).isEqualTo(UPDATED_LOGO);
    }

    @Test
    @Transactional
    void patchNonExistingRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapport))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rapport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRapport() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeDelete = rapportRepository.findAll().size();

        // Delete the rapport
        restRapportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rapport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
