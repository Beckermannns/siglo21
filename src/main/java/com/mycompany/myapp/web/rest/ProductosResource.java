package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Productos;
import com.mycompany.myapp.repository.ProductosRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Productos}.
 */
@RestController
@RequestMapping("/api/productos")
@Transactional
public class ProductosResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductosResource.class);

    private static final String ENTITY_NAME = "productos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductosRepository productosRepository;

    public ProductosResource(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    /**
     * {@code POST  /productos} : Create a new productos.
     *
     * @param productos the productos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productos, or with status {@code 400 (Bad Request)} if the productos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Productos> createProductos(@Valid @RequestBody Productos productos) throws URISyntaxException {
        LOG.debug("REST request to save Productos : {}", productos);
        if (productos.getId() != null) {
            throw new BadRequestAlertException("A new productos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productos = productosRepository.save(productos);
        return ResponseEntity.created(new URI("/api/productos/" + productos.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productos.getId().toString()))
            .body(productos);
    }

    /**
     * {@code PUT  /productos/:id} : Updates an existing productos.
     *
     * @param id the id of the productos to save.
     * @param productos the productos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productos,
     * or with status {@code 400 (Bad Request)} if the productos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Productos> updateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Productos productos
    ) throws URISyntaxException {
        LOG.debug("REST request to update Productos : {}, {}", id, productos);
        if (productos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productos = productosRepository.save(productos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productos.getId().toString()))
            .body(productos);
    }

    /**
     * {@code PATCH  /productos/:id} : Partial updates given fields of an existing productos, field will ignore if it is null
     *
     * @param id the id of the productos to save.
     * @param productos the productos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productos,
     * or with status {@code 400 (Bad Request)} if the productos is not valid,
     * or with status {@code 404 (Not Found)} if the productos is not found,
     * or with status {@code 500 (Internal Server Error)} if the productos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Productos> partialUpdateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Productos productos
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Productos partially : {}, {}", id, productos);
        if (productos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Productos> result = productosRepository
            .findById(productos.getId())
            .map(existingProductos -> {
                if (productos.getNombre() != null) {
                    existingProductos.setNombre(productos.getNombre());
                }
                if (productos.getCantidad() != null) {
                    existingProductos.setCantidad(productos.getCantidad());
                }
                if (productos.getPrecio() != null) {
                    existingProductos.setPrecio(productos.getPrecio());
                }

                return existingProductos;
            })
            .map(productosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productos.getId().toString())
        );
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("")
    public List<Productos> getAllProductos() {
        LOG.debug("REST request to get all Productos");
        return productosRepository.findAll();
    }

    /**
     * {@code GET  /productos/:id} : get the "id" productos.
     *
     * @param id the id of the productos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Productos> getProductos(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Productos : {}", id);
        Optional<Productos> productos = productosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productos);
    }

    /**
     * {@code DELETE  /productos/:id} : delete the "id" productos.
     *
     * @param id the id of the productos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductos(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Productos : {}", id);
        productosRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
