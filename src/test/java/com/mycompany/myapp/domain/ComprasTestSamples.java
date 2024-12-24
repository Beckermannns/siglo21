package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ComprasTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Compras getComprasSample1() {
        return new Compras().id(1L).detalle("detalle1").cantidad(1);
    }

    public static Compras getComprasSample2() {
        return new Compras().id(2L).detalle("detalle2").cantidad(2);
    }

    public static Compras getComprasRandomSampleGenerator() {
        return new Compras().id(longCount.incrementAndGet()).detalle(UUID.randomUUID().toString()).cantidad(intCount.incrementAndGet());
    }
}
