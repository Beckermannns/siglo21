package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Ventas;
import com.mycompany.myapp.repository.VentasRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ventas}.
 */
@RestController
@RequestMapping("/api/ventas")
@Transactional
public class VentasResource {

    private static final Logger LOG = LoggerFactory.getLogger(VentasResource.class);

    private static final String ENTITY_NAME = "ventas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentasRepository ventasRepository;

    public VentasResource(VentasRepository ventasRepository) {
        this.ventasRepository = ventasRepository;
    }

    /**
     * {@code POST  /ventas} : Create a new ventas.
     *
     * @param ventas the ventas to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ventas, or with status {@code 400 (Bad Request)} if the ventas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ventas> createVentas(@Valid @RequestBody Ventas ventas) throws URISyntaxException {
        LOG.debug("REST request to save Ventas : {}", ventas);
        if (ventas.getId() != null) {
            throw new BadRequestAlertException("A new ventas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ventas = ventasRepository.save(ventas);
        return ResponseEntity.created(new URI("/api/ventas/" + ventas.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ventas.getId().toString()))
            .body(ventas);
    }

    /**
     * {@code PUT  /ventas/:id} : Updates an existing ventas.
     *
     * @param id the id of the ventas to save.
     * @param ventas the ventas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventas,
     * or with status {@code 400 (Bad Request)} if the ventas is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ventas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ventas> updateVentas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ventas ventas
    ) throws URISyntaxException {
        LOG.debug("REST request to update Ventas : {}, {}", id, ventas);
        if (ventas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ventas = ventasRepository.save(ventas);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventas.getId().toString()))
            .body(ventas);
    }

    /**
     * {@code PATCH  /ventas/:id} : Partial updates given fields of an existing ventas, field will ignore if it is null
     *
     * @param id the id of the ventas to save.
     * @param ventas the ventas to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventas,
     * or with status {@code 400 (Bad Request)} if the ventas is not valid,
     * or with status {@code 404 (Not Found)} if the ventas is not found,
     * or with status {@code 500 (Internal Server Error)} if the ventas couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ventas> partialUpdateVentas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ventas ventas
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Ventas partially : {}, {}", id, ventas);
        if (ventas.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventas.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ventas> result = ventasRepository
            .findById(ventas.getId())
            .map(existingVentas -> {
                if (ventas.getDescripcion() != null) {
                    existingVentas.setDescripcion(ventas.getDescripcion());
                }
                if (ventas.getCantidad() != null) {
                    existingVentas.setCantidad(ventas.getCantidad());
                }
                if (ventas.getTotal() != null) {
                    existingVentas.setTotal(ventas.getTotal());
                }

                return existingVentas;
            })
            .map(ventasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventas.getId().toString())
        );
    }

    /**
     * {@code GET  /ventas} : get all the ventas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventas in body.
     */
    @GetMapping("")
    public List<Ventas> getAllVentas() {
        LOG.debug("REST request to get all Ventas");
        return ventasRepository.findAll();
    }

    /**
     * {@code GET  /ventas/:id} : get the "id" ventas.
     *
     * @param id the id of the ventas to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ventas, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ventas> getVentas(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ventas : {}", id);
        Optional<Ventas> ventas = ventasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ventas);
    }

    /**
     * {@code DELETE  /ventas/:id} : delete the "id" ventas.
     *
     * @param id the id of the ventas to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVentas(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ventas : {}", id);
        ventasRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
