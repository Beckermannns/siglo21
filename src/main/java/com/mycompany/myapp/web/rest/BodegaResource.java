package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Bodega;
import com.mycompany.myapp.repository.BodegaRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Bodega}.
 */
@RestController
@RequestMapping("/api/bodegas")
@Transactional
public class BodegaResource {

    private static final Logger LOG = LoggerFactory.getLogger(BodegaResource.class);

    private static final String ENTITY_NAME = "bodega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodegaRepository bodegaRepository;

    public BodegaResource(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    /**
     * {@code POST  /bodegas} : Create a new bodega.
     *
     * @param bodega the bodega to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bodega, or with status {@code 400 (Bad Request)} if the bodega has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Bodega> createBodega(@Valid @RequestBody Bodega bodega) throws URISyntaxException {
        LOG.debug("REST request to save Bodega : {}", bodega);
        if (bodega.getId() != null) {
            throw new BadRequestAlertException("A new bodega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bodega = bodegaRepository.save(bodega);
        return ResponseEntity.created(new URI("/api/bodegas/" + bodega.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bodega.getId().toString()))
            .body(bodega);
    }

    /**
     * {@code PUT  /bodegas/:id} : Updates an existing bodega.
     *
     * @param id the id of the bodega to save.
     * @param bodega the bodega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodega,
     * or with status {@code 400 (Bad Request)} if the bodega is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bodega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bodega> updateBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Bodega bodega
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bodega : {}, {}", id, bodega);
        if (bodega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodegaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bodega = bodegaRepository.save(bodega);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bodega.getId().toString()))
            .body(bodega);
    }

    /**
     * {@code PATCH  /bodegas/:id} : Partial updates given fields of an existing bodega, field will ignore if it is null
     *
     * @param id the id of the bodega to save.
     * @param bodega the bodega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bodega,
     * or with status {@code 400 (Bad Request)} if the bodega is not valid,
     * or with status {@code 404 (Not Found)} if the bodega is not found,
     * or with status {@code 500 (Internal Server Error)} if the bodega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bodega> partialUpdateBodega(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bodega bodega
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bodega partially : {}, {}", id, bodega);
        if (bodega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bodega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodegaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bodega> result = bodegaRepository
            .findById(bodega.getId())
            .map(existingBodega -> {
                if (bodega.getProducto() != null) {
                    existingBodega.setProducto(bodega.getProducto());
                }
                if (bodega.getCantidad() != null) {
                    existingBodega.setCantidad(bodega.getCantidad());
                }
                if (bodega.getDescripcion() != null) {
                    existingBodega.setDescripcion(bodega.getDescripcion());
                }

                return existingBodega;
            })
            .map(bodegaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bodega.getId().toString())
        );
    }

    /**
     * {@code GET  /bodegas} : get all the bodegas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodegas in body.
     */
    @GetMapping("")
    public List<Bodega> getAllBodegas() {
        LOG.debug("REST request to get all Bodegas");
        return bodegaRepository.findAll();
    }

    /**
     * {@code GET  /bodegas/:id} : get the "id" bodega.
     *
     * @param id the id of the bodega to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bodega, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bodega> getBodega(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bodega : {}", id);
        Optional<Bodega> bodega = bodegaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bodega);
    }

    /**
     * {@code DELETE  /bodegas/:id} : delete the "id" bodega.
     *
     * @param id the id of the bodega to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodega(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bodega : {}", id);
        bodegaRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
