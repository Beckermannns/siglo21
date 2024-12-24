package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Productos.class);
        Productos productos1 = getProductosSample1();
        Productos productos2 = new Productos();
        assertThat(productos1).isNotEqualTo(productos2);

        productos2.setId(productos1.getId());
        assertThat(productos1).isEqualTo(productos2);

        productos2 = getProductosSample2();
        assertThat(productos1).isNotEqualTo(productos2);
    }
}
