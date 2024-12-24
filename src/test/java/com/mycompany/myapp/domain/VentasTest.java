package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.VentasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VentasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ventas.class);
        Ventas ventas1 = getVentasSample1();
        Ventas ventas2 = new Ventas();
        assertThat(ventas1).isNotEqualTo(ventas2);

        ventas2.setId(ventas1.getId());
        assertThat(ventas1).isEqualTo(ventas2);

        ventas2 = getVentasSample2();
        assertThat(ventas1).isNotEqualTo(ventas2);
    }
}
