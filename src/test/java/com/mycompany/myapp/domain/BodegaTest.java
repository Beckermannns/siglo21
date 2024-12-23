package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BodegaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BodegaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bodega.class);
        Bodega bodega1 = getBodegaSample1();
        Bodega bodega2 = new Bodega();
        assertThat(bodega1).isNotEqualTo(bodega2);

        bodega2.setId(bodega1.getId());
        assertThat(bodega1).isEqualTo(bodega2);

        bodega2 = getBodegaSample2();
        assertThat(bodega1).isNotEqualTo(bodega2);
    }
}
