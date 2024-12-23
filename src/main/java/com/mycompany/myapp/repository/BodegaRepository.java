package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bodega;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bodega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {}
