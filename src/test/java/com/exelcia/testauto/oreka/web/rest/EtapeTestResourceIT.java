package com.exelcia.testauto.oreka.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exelcia.testauto.oreka.IntegrationTest;
import com.exelcia.testauto.oreka.domain.EtapeTest;
import com.exelcia.testauto.oreka.repository.EtapeTestRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EtapeTestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtapeTestResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_INFOS = "AAAAAAAAAA";
    private static final String UPDATED_INFOS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/etape-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtapeTestRepository etapeTestRepository;

    @Mock
    private EtapeTestRepository etapeTestRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtapeTestMockMvc;

    private EtapeTest etapeTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EtapeTest createEntity(EntityManager em) {
        EtapeTest etapeTest = new EtapeTest().nom(DEFAULT_NOM).status(DEFAULT_STATUS).infos(DEFAULT_INFOS);
        return etapeTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EtapeTest createUpdatedEntity(EntityManager em) {
        EtapeTest etapeTest = new EtapeTest().nom(UPDATED_NOM).status(UPDATED_STATUS).infos(UPDATED_INFOS);
        return etapeTest;
    }

    @BeforeEach
    public void initTest() {
        etapeTest = createEntity(em);
    }

    @Test
    @Transactional
    void createEtapeTest() throws Exception {
        int databaseSizeBeforeCreate = etapeTestRepository.findAll().size();
        // Create the EtapeTest
        restEtapeTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapeTest)))
            .andExpect(status().isCreated());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeCreate + 1);
        EtapeTest testEtapeTest = etapeTestList.get(etapeTestList.size() - 1);
        assertThat(testEtapeTest.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEtapeTest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEtapeTest.getInfos()).isEqualTo(DEFAULT_INFOS);
    }

    @Test
    @Transactional
    void createEtapeTestWithExistingId() throws Exception {
        // Create the EtapeTest with an existing ID
        etapeTest.setId(1L);

        int databaseSizeBeforeCreate = etapeTestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtapeTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapeTest)))
            .andExpect(status().isBadRequest());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = etapeTestRepository.findAll().size();
        // set the field null
        etapeTest.setNom(null);

        // Create the EtapeTest, which fails.

        restEtapeTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapeTest)))
            .andExpect(status().isBadRequest());

        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtapeTests() throws Exception {
        // Initialize the database
        etapeTestRepository.saveAndFlush(etapeTest);

        // Get all the etapeTestList
        restEtapeTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etapeTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].infos").value(hasItem(DEFAULT_INFOS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtapeTestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(etapeTestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtapeTestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(etapeTestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtapeTestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(etapeTestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtapeTestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(etapeTestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEtapeTest() throws Exception {
        // Initialize the database
        etapeTestRepository.saveAndFlush(etapeTest);

        // Get the etapeTest
        restEtapeTestMockMvc
            .perform(get(ENTITY_API_URL_ID, etapeTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etapeTest.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.infos").value(DEFAULT_INFOS));
    }

    @Test
    @Transactional
    void getNonExistingEtapeTest() throws Exception {
        // Get the etapeTest
        restEtapeTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtapeTest() throws Exception {
        // Initialize the database
        etapeTestRepository.saveAndFlush(etapeTest);

        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();

        // Update the etapeTest
        EtapeTest updatedEtapeTest = etapeTestRepository.findById(etapeTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtapeTest are not directly saved in db
        em.detach(updatedEtapeTest);
        updatedEtapeTest.nom(UPDATED_NOM).status(UPDATED_STATUS).infos(UPDATED_INFOS);

        restEtapeTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtapeTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtapeTest))
            )
            .andExpect(status().isOk());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
        EtapeTest testEtapeTest = etapeTestList.get(etapeTestList.size() - 1);
        assertThat(testEtapeTest.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtapeTest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEtapeTest.getInfos()).isEqualTo(UPDATED_INFOS);
    }

    @Test
    @Transactional
    void putNonExistingEtapeTest() throws Exception {
        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();
        etapeTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtapeTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etapeTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etapeTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtapeTest() throws Exception {
        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();
        etapeTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapeTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etapeTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtapeTest() throws Exception {
        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();
        etapeTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapeTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etapeTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtapeTestWithPatch() throws Exception {
        // Initialize the database
        etapeTestRepository.saveAndFlush(etapeTest);

        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();

        // Update the etapeTest using partial update
        EtapeTest partialUpdatedEtapeTest = new EtapeTest();
        partialUpdatedEtapeTest.setId(etapeTest.getId());

        restEtapeTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtapeTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtapeTest))
            )
            .andExpect(status().isOk());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
        EtapeTest testEtapeTest = etapeTestList.get(etapeTestList.size() - 1);
        assertThat(testEtapeTest.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEtapeTest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEtapeTest.getInfos()).isEqualTo(DEFAULT_INFOS);
    }

    @Test
    @Transactional
    void fullUpdateEtapeTestWithPatch() throws Exception {
        // Initialize the database
        etapeTestRepository.saveAndFlush(etapeTest);

        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();

        // Update the etapeTest using partial update
        EtapeTest partialUpdatedEtapeTest = new EtapeTest();
        partialUpdatedEtapeTest.setId(etapeTest.getId());

        partialUpdatedEtapeTest.nom(UPDATED_NOM).status(UPDATED_STATUS).infos(UPDATED_INFOS);

        restEtapeTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtapeTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtapeTest))
            )
            .andExpect(status().isOk());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
        EtapeTest testEtapeTest = etapeTestList.get(etapeTestList.size() - 1);
        assertThat(testEtapeTest.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtapeTest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEtapeTest.getInfos()).isEqualTo(UPDATED_INFOS);
    }

    @Test
    @Transactional
    void patchNonExistingEtapeTest() throws Exception {
        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();
        etapeTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtapeTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etapeTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etapeTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtapeTest() throws Exception {
        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();
        etapeTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapeTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etapeTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtapeTest() throws Exception {
        int databaseSizeBeforeUpdate = etapeTestRepository.findAll().size();
        etapeTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtapeTestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etapeTest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EtapeTest in the database
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtapeTest() throws Exception {
        // Initialize the database
        etapeTestRepository.saveAndFlush(etapeTest);

        int databaseSizeBeforeDelete = etapeTestRepository.findAll().size();

        // Delete the etapeTest
        restEtapeTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, etapeTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EtapeTest> etapeTestList = etapeTestRepository.findAll();
        assertThat(etapeTestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
