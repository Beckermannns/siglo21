package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Bodega.
 */
@Entity
@Table(name = "bodega")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bodega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "producto", nullable = false)
    private String producto;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bodega id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProducto() {
        return this.producto;
    }

    public Bodega producto(String producto) {
        this.setProducto(producto);
        return this;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Bodega cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Bodega descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bodega)) {
            return false;
        }
        return getId() != null && getId().equals(((Bodega) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bodega{" +
            "id=" + getId() +
            ", producto='" + getProducto() + "'" +
            ", cantidad=" + getCantidad() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
