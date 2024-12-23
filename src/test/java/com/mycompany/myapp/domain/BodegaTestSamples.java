package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BodegaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Bodega getBodegaSample1() {
        return new Bodega().id(1L).producto("producto1").cantidad(1).descripcion("descripcion1");
    }

    public static Bodega getBodegaSample2() {
        return new Bodega().id(2L).producto("producto2").cantidad(2).descripcion("descripcion2");
    }

    public static Bodega getBodegaRandomSampleGenerator() {
        return new Bodega()
            .id(longCount.incrementAndGet())
            .producto(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString());
    }
}
