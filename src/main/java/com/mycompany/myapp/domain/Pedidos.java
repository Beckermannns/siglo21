package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Pedidos.
 */
@Entity
@Table(name = "pedidos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pedidos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "estado")
    private Boolean estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pedidos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Pedidos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return this.estado;
    }

    public Pedidos estado(Boolean estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedidos)) {
            return false;
        }
        return getId() != null && getId().equals(((Pedidos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedidos{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
