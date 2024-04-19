package com.example.lab3.repository;

import com.example.lab3.entity.Oftalmologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OftalmologoRepository extends JpaRepository<Oftalmologo,Integer> {

    List<Oftalmologo> buscarPorClinicaId(int clinica_id);
}
