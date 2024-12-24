package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VentasTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ventas getVentasSample1() {
        return new Ventas().id(1L).descripcion("descripcion1").cantidad(1);
    }

    public static Ventas getVentasSample2() {
        return new Ventas().id(2L).descripcion("descripcion2").cantidad(2);
    }

    public static Ventas getVentasRandomSampleGenerator() {
        return new Ventas().id(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString()).cantidad(intCount.incrementAndGet());
    }
}
