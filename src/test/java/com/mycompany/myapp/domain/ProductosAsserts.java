package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductosAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductosAllPropertiesEquals(Productos expected, Productos actual) {
        assertProductosAutoGeneratedPropertiesEquals(expected, actual);
        assertProductosAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductosAllUpdatablePropertiesEquals(Productos expected, Productos actual) {
        assertProductosUpdatableFieldsEquals(expected, actual);
        assertProductosUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductosAutoGeneratedPropertiesEquals(Productos expected, Productos actual) {
        assertThat(expected)
            .as("Verify Productos auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductosUpdatableFieldsEquals(Productos expected, Productos actual) {
        assertThat(expected)
            .as("Verify Productos relevant properties")
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getCantidad()).as("check cantidad").isEqualTo(actual.getCantidad()))
            .satisfies(e -> assertThat(e.getPrecio()).as("check precio").isEqualTo(actual.getPrecio()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductosUpdatableRelationshipsEquals(Productos expected, Productos actual) {
        // empty method
    }
}