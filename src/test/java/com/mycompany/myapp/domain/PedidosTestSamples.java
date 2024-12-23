package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PedidosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pedidos getPedidosSample1() {
        return new Pedidos().id(1L).descripcion("descripcion1");
    }

    public static Pedidos getPedidosSample2() {
        return new Pedidos().id(2L).descripcion("descripcion2");
    }

    public static Pedidos getPedidosRandomSampleGenerator() {
        return new Pedidos().id(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
