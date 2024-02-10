package com.exelcia.testauto.oreka.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exelcia.testauto.oreka.IntegrationTest;
import com.exelcia.testauto.oreka.domain.TestAuto;
import com.exelcia.testauto.oreka.repository.TestAutoRepository;
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
 * Integration tests for the {@link TestAutoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestAutoResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    private static final String DEFAULT_INFOS = "AAAAAAAAAA";
    private static final String UPDATED_INFOS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-autos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestAutoRepository testAutoRepository;

    @Mock
    private TestAutoRepository testAutoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestAutoMockMvc;

    private TestAuto testAuto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestAuto createEntity(EntityManager em) {
        TestAuto testAuto = new TestAuto().nom(DEFAULT_NOM).status(DEFAULT_STATUS).categorie(DEFAULT_CATEGORIE).infos(DEFAULT_INFOS);
        return testAuto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestAuto createUpdatedEntity(EntityManager em) {
        TestAuto testAuto = new TestAuto().nom(UPDATED_NOM).status(UPDATED_STATUS).categorie(UPDATED_CATEGORIE).infos(UPDATED_INFOS);
        return testAuto;
    }

    @BeforeEach
    public void initTest() {
        testAuto = createEntity(em);
    }

    @Test
    @Transactional
    void createTestAuto() throws Exception {
        int databaseSizeBeforeCreate = testAutoRepository.findAll().size();
        // Create the TestAuto
        restTestAutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAuto)))
            .andExpect(status().isCreated());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeCreate + 1);
        TestAuto testTestAuto = testAutoList.get(testAutoList.size() - 1);
        assertThat(testTestAuto.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testTestAuto.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTestAuto.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testTestAuto.getInfos()).isEqualTo(DEFAULT_INFOS);
    }

    @Test
    @Transactional
    void createTestAutoWithExistingId() throws Exception {
        // Create the TestAuto with an existing ID
        testAuto.setId(1L);

        int databaseSizeBeforeCreate = testAutoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestAutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAuto)))
            .andExpect(status().isBadRequest());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = testAutoRepository.findAll().size();
        // set the field null
        testAuto.setNom(null);

        // Create the TestAuto, which fails.

        restTestAutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAuto)))
            .andExpect(status().isBadRequest());

        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestAutos() throws Exception {
        // Initialize the database
        testAutoRepository.saveAndFlush(testAuto);

        // Get all the testAutoList
        restTestAutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testAuto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].infos").value(hasItem(DEFAULT_INFOS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestAutosWithEagerRelationshipsIsEnabled() throws Exception {
        when(testAutoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestAutoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testAutoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestAutosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(testAutoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestAutoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(testAutoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTestAuto() throws Exception {
        // Initialize the database
        testAutoRepository.saveAndFlush(testAuto);

        // Get the testAuto
        restTestAutoMockMvc
            .perform(get(ENTITY_API_URL_ID, testAuto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testAuto.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE))
            .andExpect(jsonPath("$.infos").value(DEFAULT_INFOS));
    }

    @Test
    @Transactional
    void getNonExistingTestAuto() throws Exception {
        // Get the testAuto
        restTestAutoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestAuto() throws Exception {
        // Initialize the database
        testAutoRepository.saveAndFlush(testAuto);

        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();

        // Update the testAuto
        TestAuto updatedTestAuto = testAutoRepository.findById(testAuto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestAuto are not directly saved in db
        em.detach(updatedTestAuto);
        updatedTestAuto.nom(UPDATED_NOM).status(UPDATED_STATUS).categorie(UPDATED_CATEGORIE).infos(UPDATED_INFOS);

        restTestAutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestAuto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestAuto))
            )
            .andExpect(status().isOk());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
        TestAuto testTestAuto = testAutoList.get(testAutoList.size() - 1);
        assertThat(testTestAuto.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testTestAuto.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTestAuto.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testTestAuto.getInfos()).isEqualTo(UPDATED_INFOS);
    }

    @Test
    @Transactional
    void putNonExistingTestAuto() throws Exception {
        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();
        testAuto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestAutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testAuto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAuto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestAuto() throws Exception {
        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();
        testAuto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAuto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestAuto() throws Exception {
        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();
        testAuto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAutoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAuto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestAutoWithPatch() throws Exception {
        // Initialize the database
        testAutoRepository.saveAndFlush(testAuto);

        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();

        // Update the testAuto using partial update
        TestAuto partialUpdatedTestAuto = new TestAuto();
        partialUpdatedTestAuto.setId(testAuto.getId());

        partialUpdatedTestAuto.categorie(UPDATED_CATEGORIE).infos(UPDATED_INFOS);

        restTestAutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestAuto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestAuto))
            )
            .andExpect(status().isOk());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
        TestAuto testTestAuto = testAutoList.get(testAutoList.size() - 1);
        assertThat(testTestAuto.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testTestAuto.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTestAuto.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testTestAuto.getInfos()).isEqualTo(UPDATED_INFOS);
    }

    @Test
    @Transactional
    void fullUpdateTestAutoWithPatch() throws Exception {
        // Initialize the database
        testAutoRepository.saveAndFlush(testAuto);

        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();

        // Update the testAuto using partial update
        TestAuto partialUpdatedTestAuto = new TestAuto();
        partialUpdatedTestAuto.setId(testAuto.getId());

        partialUpdatedTestAuto.nom(UPDATED_NOM).status(UPDATED_STATUS).categorie(UPDATED_CATEGORIE).infos(UPDATED_INFOS);

        restTestAutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestAuto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestAuto))
            )
            .andExpect(status().isOk());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
        TestAuto testTestAuto = testAutoList.get(testAutoList.size() - 1);
        assertThat(testTestAuto.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testTestAuto.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTestAuto.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testTestAuto.getInfos()).isEqualTo(UPDATED_INFOS);
    }

    @Test
    @Transactional
    void patchNonExistingTestAuto() throws Exception {
        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();
        testAuto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestAutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testAuto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testAuto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestAuto() throws Exception {
        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();
        testAuto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testAuto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestAuto() throws Exception {
        int databaseSizeBeforeUpdate = testAutoRepository.findAll().size();
        testAuto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAutoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testAuto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestAuto in the database
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestAuto() throws Exception {
        // Initialize the database
        testAutoRepository.saveAndFlush(testAuto);

        int databaseSizeBeforeDelete = testAutoRepository.findAll().size();

        // Delete the testAuto
        restTestAutoMockMvc
            .perform(delete(ENTITY_API_URL_ID, testAuto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestAuto> testAutoList = testAutoRepository.findAll();
        assertThat(testAutoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
