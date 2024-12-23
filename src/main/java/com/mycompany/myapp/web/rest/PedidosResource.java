package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pedidos;
import com.mycompany.myapp.repository.PedidosRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Pedidos}.
 */
@RestController
@RequestMapping("/api/pedidos")
@Transactional
public class PedidosResource {

    private static final Logger LOG = LoggerFactory.getLogger(PedidosResource.class);

    private static final String ENTITY_NAME = "pedidos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PedidosRepository pedidosRepository;

    public PedidosResource(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    /**
     * {@code POST  /pedidos} : Create a new pedidos.
     *
     * @param pedidos the pedidos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new pedidos, or with status {@code 400 (Bad Request)} if the
     *         pedidos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pedidos> createPedidos(@Valid @RequestBody Pedidos pedidos) throws URISyntaxException {
        LOG.debug("REST request to save Pedidos : {}", pedidos);
        if (pedidos.getId() != null) {
            throw new BadRequestAlertException("A new pedidos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pedidos = pedidosRepository.save(pedidos);
        return ResponseEntity.created(new URI("/api/pedidos/" + pedidos.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        pedidos.getId().toString()))
                .body(pedidos);
    }

    /**
     * {@code PUT  /pedidos/:id} : Updates an existing pedidos.
     *
     * @param id      the id of the pedidos to save.
     * @param pedidos the pedidos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated pedidos,
     *         or with status {@code 400 (Bad Request)} if the pedidos is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the pedidos
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedidos> updatePedidos(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody Pedidos pedidos) throws URISyntaxException {
        LOG.debug("REST request to update Pedidos : {}, {}", id, pedidos);
        if (pedidos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pedidos = pedidosRepository.save(pedidos);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        pedidos.getId().toString()))
                .body(pedidos);
    }

    /**
     * {@code PATCH  /pedidos/:id} : Partial updates given fields of an existing
     * pedidos, field will ignore if it is null
     *
     * @param id      the id of the pedidos to save.
     * @param pedidos the pedidos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated pedidos,
     *         or with status {@code 400 (Bad Request)} if the pedidos is not valid,
     *         or with status {@code 404 (Not Found)} if the pedidos is not found,
     *         or with status {@code 500 (Internal Server Error)} if the pedidos
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pedidos> partialUpdatePedidos(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody Pedidos pedidos) throws URISyntaxException {
        LOG.debug("REST request to partial update Pedidos partially : {}, {}", id, pedidos);
        if (pedidos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pedidos> result = pedidosRepository
                .findById(pedidos.getId())
                .map(existingPedidos -> {
                    if (pedidos.getDescripcion() != null) {
                        existingPedidos.setDescripcion(pedidos.getDescripcion());
                    }
                    if (pedidos.getEstado() != null) {
                        existingPedidos.setEstado(pedidos.getEstado());
                    }

                    return existingPedidos;
                })
                .map(pedidosRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidos.getId().toString()));
    }

    /**
     * {@code GET  /pedidos} : get all the pedidos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of pedidos in body.
     */
    @GetMapping("")
    public List<Pedidos> getAllPedidos() {
        LOG.debug("REST request to get all Pedidos");
        return pedidosRepository.findAll();
    }

    /**
     * {@code GET  /pedidos/:id} : get the "id" pedidos.
     *
     * @param id the id of the pedidos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the pedidos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedidos> getPedidos(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pedidos : {}", id);
        Optional<Pedidos> pedidos = pedidosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pedidos);
    }

    /**
     * {@code DELETE  /pedidos/:id} : delete the "id" pedidos.
     *
     * @param id the id of the pedidos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedidos(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pedidos : {}", id);
        pedidosRepository.deleteById(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
