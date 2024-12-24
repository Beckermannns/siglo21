package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Productos getProductosSample1() {
        return new Productos().id(1L).nombre("nombre1").cantidad(1);
    }

    public static Productos getProductosSample2() {
        return new Productos().id(2L).nombre("nombre2").cantidad(2);
    }

    public static Productos getProductosRandomSampleGenerator() {
        return new Productos().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).cantidad(intCount.incrementAndGet());
    }
}
