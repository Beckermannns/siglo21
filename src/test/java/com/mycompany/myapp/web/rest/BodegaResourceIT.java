package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.BodegaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.repository.BodegaRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BodegaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BodegaResourceIT {

    private static final String DEFAULT_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bodegas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BodegaRepository bodegaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBodegaMockMvc;

    private Bodega bodega;

    private Bodega insertedBodega;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bodega createEntity() {
        return new Bodega().producto(DEFAULT_PRODUCTO).cantidad(DEFAULT_CANTIDAD).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bodega createUpdatedEntity() {
        return new Bodega().producto(UPDATED_PRODUCTO).cantidad(UPDATED_CANTIDAD).descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    public void initTest() {
        bodega = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBodega != null) {
            bodegaRepository.delete(insertedBodega);
            insertedBodega = null;
        }
    }

    @Test
    @Transactional
    void createBodega() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bodega
        var returnedBodega = om.readValue(
            restBodegaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Bodega.class
        );

        // Validate the Bodega in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBodegaUpdatableFieldsEquals(returnedBodega, getPersistedBodega(returnedBodega));

        insertedBodega = returnedBodega;
    }

    @Test
    @Transactional
    void createBodegaWithExistingId() throws Exception {
        // Create the Bodega with an existing ID
        bodega.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bodega.setProducto(null);

        // Create the Bodega, which fails.

        restBodegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bodega.setCantidad(null);

        // Create the Bodega, which fails.

        restBodegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bodega.setDescripcion(null);

        // Create the Bodega, which fails.

        restBodegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBodegas() throws Exception {
        // Initialize the database
        insertedBodega = bodegaRepository.saveAndFlush(bodega);

        // Get all the bodegaList
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bodega.getId().intValue())))
            .andExpect(jsonPath("$.[*].producto").value(hasItem(DEFAULT_PRODUCTO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getBodega() throws Exception {
        // Initialize the database
        insertedBodega = bodegaRepository.saveAndFlush(bodega);

        // Get the bodega
        restBodegaMockMvc
            .perform(get(ENTITY_API_URL_ID, bodega.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bodega.getId().intValue()))
            .andExpect(jsonPath("$.producto").value(DEFAULT_PRODUCTO))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingBodega() throws Exception {
        // Get the bodega
        restBodegaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBodega() throws Exception {
        // Initialize the database
        insertedBodega = bodegaRepository.saveAndFlush(bodega);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodega
        Bodega updatedBodega = bodegaRepository.findById(bodega.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBodega are not directly saved in db
        em.detach(updatedBodega);
        updatedBodega.producto(UPDATED_PRODUCTO).cantidad(UPDATED_CANTIDAD).descripcion(UPDATED_DESCRIPCION);

        restBodegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBodega.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBodega))
            )
            .andExpect(status().isOk());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBodegaToMatchAllProperties(updatedBodega);
    }

    @Test
    @Transactional
    void putNonExistingBodega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodega.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(put(ENTITY_API_URL_ID, bodega.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBodega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBodega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBodegaWithPatch() throws Exception {
        // Initialize the database
        insertedBodega = bodegaRepository.saveAndFlush(bodega);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodega using partial update
        Bodega partialUpdatedBodega = new Bodega();
        partialUpdatedBodega.setId(bodega.getId());

        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodega.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodega))
            )
            .andExpect(status().isOk());

        // Validate the Bodega in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodegaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBodega, bodega), getPersistedBodega(bodega));
    }

    @Test
    @Transactional
    void fullUpdateBodegaWithPatch() throws Exception {
        // Initialize the database
        insertedBodega = bodegaRepository.saveAndFlush(bodega);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bodega using partial update
        Bodega partialUpdatedBodega = new Bodega();
        partialUpdatedBodega.setId(bodega.getId());

        partialUpdatedBodega.producto(UPDATED_PRODUCTO).cantidad(UPDATED_CANTIDAD).descripcion(UPDATED_DESCRIPCION);

        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBodega.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBodega))
            )
            .andExpect(status().isOk());

        // Validate the Bodega in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBodegaUpdatableFieldsEquals(partialUpdatedBodega, getPersistedBodega(partialUpdatedBodega));
    }

    @Test
    @Transactional
    void patchNonExistingBodega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodega.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bodega.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBodega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bodega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBodega() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bodega.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodegaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bodega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bodega in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBodega() throws Exception {
        // Initialize the database
        insertedBodega = bodegaRepository.saveAndFlush(bodega);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bodega
        restBodegaMockMvc
            .perform(delete(ENTITY_API_URL_ID, bodega.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bodegaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Bodega getPersistedBodega(Bodega bodega) {
        return bodegaRepository.findById(bodega.getId()).orElseThrow();
    }

    protected void assertPersistedBodegaToMatchAllProperties(Bodega expectedBodega) {
        assertBodegaAllPropertiesEquals(expectedBodega, getPersistedBodega(expectedBodega));
    }

    protected void assertPersistedBodegaToMatchUpdatableProperties(Bodega expectedBodega) {
        assertBodegaAllUpdatablePropertiesEquals(expectedBodega, getPersistedBodega(expectedBodega));
    }
}
