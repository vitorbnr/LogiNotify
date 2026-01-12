package com.vitorbnr.LogiNotify.repository;

import com.vitorbnr.LogiNotify.domain.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
}
