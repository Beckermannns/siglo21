package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ComprasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComprasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compras.class);
        Compras compras1 = getComprasSample1();
        Compras compras2 = new Compras();
        assertThat(compras1).isNotEqualTo(compras2);

        compras2.setId(compras1.getId());
        assertThat(compras1).isEqualTo(compras2);

        compras2 = getComprasSample2();
        assertThat(compras1).isNotEqualTo(compras2);
    }
}
