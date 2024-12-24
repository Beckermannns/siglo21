package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.VentasAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ventas;
import com.mycompany.myapp.repository.VentasRepository;
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
 * Integration tests for the {@link VentasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VentasResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Float DEFAULT_TOTAL = 1F;
    private static final Float UPDATED_TOTAL = 2F;

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentasMockMvc;

    private Ventas ventas;

    private Ventas insertedVentas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ventas createEntity() {
        return new Ventas().descripcion(DEFAULT_DESCRIPCION).cantidad(DEFAULT_CANTIDAD).total(DEFAULT_TOTAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ventas createUpdatedEntity() {
        return new Ventas().descripcion(UPDATED_DESCRIPCION).cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);
    }

    @BeforeEach
    public void initTest() {
        ventas = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVentas != null) {
            ventasRepository.delete(insertedVentas);
            insertedVentas = null;
        }
    }

    @Test
    @Transactional
    void createVentas() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ventas
        var returnedVentas = om.readValue(
            restVentasMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Ventas.class
        );

        // Validate the Ventas in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVentasUpdatableFieldsEquals(returnedVentas, getPersistedVentas(returnedVentas));

        insertedVentas = returnedVentas;
    }

    @Test
    @Transactional
    void createVentasWithExistingId() throws Exception {
        // Create the Ventas with an existing ID
        ventas.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventas.setDescripcion(null);

        // Create the Ventas, which fails.

        restVentasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventas.setCantidad(null);

        // Create the Ventas, which fails.

        restVentasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ventas.setTotal(null);

        // Create the Ventas, which fails.

        restVentasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVentas() throws Exception {
        // Initialize the database
        insertedVentas = ventasRepository.saveAndFlush(ventas);

        // Get all the ventasList
        restVentasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventas.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getVentas() throws Exception {
        // Initialize the database
        insertedVentas = ventasRepository.saveAndFlush(ventas);

        // Get the ventas
        restVentasMockMvc
            .perform(get(ENTITY_API_URL_ID, ventas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ventas.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingVentas() throws Exception {
        // Get the ventas
        restVentasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVentas() throws Exception {
        // Initialize the database
        insertedVentas = ventasRepository.saveAndFlush(ventas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ventas
        Ventas updatedVentas = ventasRepository.findById(ventas.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVentas are not directly saved in db
        em.detach(updatedVentas);
        updatedVentas.descripcion(UPDATED_DESCRIPCION).cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restVentasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVentas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVentas))
            )
            .andExpect(status().isOk());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVentasToMatchAllProperties(updatedVentas);
    }

    @Test
    @Transactional
    void putNonExistingVentas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventas.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(put(ENTITY_API_URL_ID, ventas.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVentas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ventas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVentas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentasWithPatch() throws Exception {
        // Initialize the database
        insertedVentas = ventasRepository.saveAndFlush(ventas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ventas using partial update
        Ventas partialUpdatedVentas = new Ventas();
        partialUpdatedVentas.setId(ventas.getId());

        partialUpdatedVentas.cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVentas))
            )
            .andExpect(status().isOk());

        // Validate the Ventas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentasUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVentas, ventas), getPersistedVentas(ventas));
    }

    @Test
    @Transactional
    void fullUpdateVentasWithPatch() throws Exception {
        // Initialize the database
        insertedVentas = ventasRepository.saveAndFlush(ventas);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ventas using partial update
        Ventas partialUpdatedVentas = new Ventas();
        partialUpdatedVentas.setId(ventas.getId());

        partialUpdatedVentas.descripcion(UPDATED_DESCRIPCION).cantidad(UPDATED_CANTIDAD).total(UPDATED_TOTAL);

        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentas.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVentas))
            )
            .andExpect(status().isOk());

        // Validate the Ventas in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentasUpdatableFieldsEquals(partialUpdatedVentas, getPersistedVentas(partialUpdatedVentas));
    }

    @Test
    @Transactional
    void patchNonExistingVentas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventas.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ventas.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ventas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVentas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVentas() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ventas.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ventas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ventas in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVentas() throws Exception {
        // Initialize the database
        insertedVentas = ventasRepository.saveAndFlush(ventas);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ventas
        restVentasMockMvc
            .perform(delete(ENTITY_API_URL_ID, ventas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ventasRepository.count();
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

    protected Ventas getPersistedVentas(Ventas ventas) {
        return ventasRepository.findById(ventas.getId()).orElseThrow();
    }

    protected void assertPersistedVentasToMatchAllProperties(Ventas expectedVentas) {
        assertVentasAllPropertiesEquals(expectedVentas, getPersistedVentas(expectedVentas));
    }

    protected void assertPersistedVentasToMatchUpdatableProperties(Ventas expectedVentas) {
        assertVentasAllUpdatablePropertiesEquals(expectedVentas, getPersistedVentas(expectedVentas));
    }
}
