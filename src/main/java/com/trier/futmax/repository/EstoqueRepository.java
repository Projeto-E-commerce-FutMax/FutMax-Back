package com.trier.futmax.repository;

import com.trier.futmax.model.EstoqueModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<EstoqueModel, Long> {

}
