package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Compras;
import com.mycompany.myapp.repository.ComprasRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Compras}.
 */
@RestController
@RequestMapping("/api/compras")
@Transactional
public class ComprasResource {

    private static final Logger LOG = LoggerFactory.getLogger(ComprasResource.class);

    private static final String ENTITY_NAME = "compras";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComprasRepository comprasRepository;

    public ComprasResource(ComprasRepository comprasRepository) {
        this.comprasRepository = comprasRepository;
    }

    /**
     * {@code POST  /compras} : Create a new compras.
     *
     * @param compras the compras to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compras, or with status {@code 400 (Bad Request)} if the compras has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Compras> createCompras(@Valid @RequestBody Compras compras) throws URISyntaxException {
        LOG.debug("REST request to save Compras : {}", compras);
        if (compras.getId() != null) {
            throw new BadRequestAlertException("A new compras cannot already have an ID", ENTITY_NAME, "idexists");
        }
        compras = comprasRepository.save(compras);
        return ResponseEntity.created(new URI("/api/compras/" + compras.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, compras.getId().toString()))
            .body(compras);
    }

    /**
     * {@code PUT  /compras/:id} : Updates an existing compras.
     *
     * @param id the id of the compras to save.
     * @param compras the compras to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compras,
     * or with status {@code 400 (Bad Request)} if the compras is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compras couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Compras> updateCompras(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Compras compras
    ) throws URISyntaxException {
        LOG.debug("REST request to update Compras : {}, {}", id, compras);
        if (compras.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compras.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comprasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        compras = comprasRepository.save(compras);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compras.getId().toString()))
            .body(compras);
    }

    /**
     * {@code PATCH  /compras/:id} : Partial updates given fields of an existing compras, field will ignore if it is null
     *
     * @param id the id of the compras to save.
     * @param compras the compras to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compras,
     * or with status {@code 400 (Bad Request)} if the compras is not valid,
     * or with status {@code 404 (Not Found)} if the compras is not found,
     * or with status {@code 500 (Internal Server Error)} if the compras couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Compras> partialUpdateCompras(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Compras compras
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Compras partially : {}, {}", id, compras);
        if (compras.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compras.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comprasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Compras> result = comprasRepository
            .findById(compras.getId())
            .map(existingCompras -> {
                if (compras.getDetalle() != null) {
                    existingCompras.setDetalle(compras.getDetalle());
                }
                if (compras.getCantidad() != null) {
                    existingCompras.setCantidad(compras.getCantidad());
                }
                if (compras.getPrecio() != null) {
                    existingCompras.setPrecio(compras.getPrecio());
                }

                return existingCompras;
            })
            .map(comprasRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compras.getId().toString())
        );
    }

    /**
     * {@code GET  /compras} : get all the compras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compras in body.
     */
    @GetMapping("")
    public List<Compras> getAllCompras() {
        LOG.debug("REST request to get all Compras");
        return comprasRepository.findAll();
    }

    /**
     * {@code GET  /compras/:id} : get the "id" compras.
     *
     * @param id the id of the compras to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compras, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Compras> getCompras(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Compras : {}", id);
        Optional<Compras> compras = comprasRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(compras);
    }

    /**
     * {@code DELETE  /compras/:id} : delete the "id" compras.
     *
     * @param id the id of the compras to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompras(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Compras : {}", id);
        comprasRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
