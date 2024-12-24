package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ComprasAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Compras;
import com.mycompany.myapp.repository.ComprasRepository;
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
 * Integration tests for the {@link ComprasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComprasResourceIT {

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComprasMockMvc;

    private Compras compras;

    private Compras insertedCompras;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compras createEntity() {
        return new Compras().detalle(DEFAULT_DETALLE).cantidad(DEFAULT_CANTIDAD).precio(DEFAULT_PRECIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compras createUpdatedEntity() {
        return new Compras().detalle(UPDATED_DETALLE).cantidad(UPDATED_CANTIDAD).precio(UPDATED_PRECIO);
    }

    @BeforeEach
    public void initTest() {
        compras = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompras != null) {
            comprasRepository.delete(insertedCompras);
            insertedCompras = null;
        }
    }

    @Test
    @Transactional
    void createCompras() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compras
        var returnedCompras = om.readValue(
            restComprasMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Compras.class
        );

        // Validate the Compras in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertComprasUpdatableFieldsEquals(returnedCompras, getPersistedCompras(returnedCompras));

        insertedCompras = returnedCompras;
    }

    @Test
    @Transactional
    void createComprasWithExistingId() throws Exception {
        // Create the Compras with an existing ID
        compras.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComprasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDetalleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compras.setDetalle(null);

        // Create the Compras, which fails.

        restComprasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compras.setCantidad(null);

        // Create the Compras, which fails.

        restComprasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compras.setPrecio(null);

        // Create the Compras, which fails.

        restComprasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        insertedCompras = comprasRepository.saveAndFlush(compras);

        // Get all the comprasList
        restComprasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compras.getId().intValue())))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));
    }

    @Test
    @Transactional
    void getCompras() throws Exception {
        // Initialize the database
        insertedCompras = comprasRepository.saveAndFlush(compras);

        // Get the compras
        restComprasMockMvc
            .perform(get(ENTITY_API_URL_ID, compras.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compras.getId().intValue()))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompras() throws Exception {
        // Get the compras
        restComprasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompras() throws Exception {
        // Initialize the database
        insertedCompras = comprasRepository.saveAndFlush(compras);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compras
        Compras updatedCompras = comprasRepository.findById(compras.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompras are not directly saved in db
        em.detach(updatedCompras);
        updatedCompras.detalle(UPDATED_DETALLE).cantidad(UPDATED_CANTIDAD).precio(UPDATED_PRECIO);

        restComprasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompras.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCompras))
            )
            .andExpect(status().isOk());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedComprasToMatchAllProperties(updatedCompras);
    }

    @Test
    @Transactional
    void putNonExistingCompras() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compras.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(put(ENTITY_API_URL_ID, compras.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompras() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compras.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compras))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompras() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compras.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compras)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComprasWithPatch() throws Exception {
        // Initialize the database
        insertedCompras = comprasRepository.saveAndFlush(compras);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compras using partial update
        Compras partialUpdatedCompras = new Compras();
        partialUpdatedCompras.setId(compras.getId());

        partialUpdatedCompras.detalle(UPDATED_DETALLE).cantidad(UPDATED_CANTIDAD).precio(UPDATED_PRECIO);

        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompras.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompras))
            )
            .andExpect(status().isOk());

        // Validate the Compras in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComprasUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompras, compras), getPersistedCompras(compras));
    }

    @Test
    @Transactional
    void fullUpdateComprasWithPatch() throws Exception {
        // Initialize the database
        insertedCompras = comprasRepository.saveAndFlush(compras);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compras using partial update
        Compras partialUpdatedCompras = new Compras();
        partialUpdatedCompras.setId(compras.getId());

        partialUpdatedCompras.detalle(UPDATED_DETALLE).cantidad(UPDATED_CANTIDAD).precio(UPDATED_PRECIO);

        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompras.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompras))
            )
            .andExpect(status().isOk());

        // Validate the Compras in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComprasUpdatableFieldsEquals(partialUpdatedCompras, getPersistedCompras(partialUpdatedCompras));
    }

    @Test
    @Transactional
    void patchNonExistingCompras() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compras.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compras.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compras))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompras() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compras.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compras))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompras() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compras.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compras)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compras in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompras() throws Exception {
        // Initialize the database
        insertedCompras = comprasRepository.saveAndFlush(compras);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compras
        restComprasMockMvc
            .perform(delete(ENTITY_API_URL_ID, compras.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return comprasRepository.count();
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

    protected Compras getPersistedCompras(Compras compras) {
        return comprasRepository.findById(compras.getId()).orElseThrow();
    }

    protected void assertPersistedComprasToMatchAllProperties(Compras expectedCompras) {
        assertComprasAllPropertiesEquals(expectedCompras, getPersistedCompras(expectedCompras));
    }

    protected void assertPersistedComprasToMatchUpdatableProperties(Compras expectedCompras) {
        assertComprasAllUpdatablePropertiesEquals(expectedCompras, getPersistedCompras(expectedCompras));
    }
}
