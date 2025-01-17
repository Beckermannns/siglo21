package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PedidosAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPedidosAllPropertiesEquals(Pedidos expected, Pedidos actual) {
        assertPedidosAutoGeneratedPropertiesEquals(expected, actual);
        assertPedidosAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPedidosAllUpdatablePropertiesEquals(Pedidos expected, Pedidos actual) {
        assertPedidosUpdatableFieldsEquals(expected, actual);
        assertPedidosUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPedidosAutoGeneratedPropertiesEquals(Pedidos expected, Pedidos actual) {
        assertThat(expected)
            .as("Verify Pedidos auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPedidosUpdatableFieldsEquals(Pedidos expected, Pedidos actual) {
        assertThat(expected)
            .as("Verify Pedidos relevant properties")
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()))
            .satisfies(e -> assertThat(e.getEstado()).as("check estado").isEqualTo(actual.getEstado()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPedidosUpdatableRelationshipsEquals(Pedidos expected, Pedidos actual) {
        // empty method
    }
}
