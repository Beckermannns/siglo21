package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pedidos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pedidos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Long> {}
