package com.example.lab3.repository;

import com.example.lab3.entity.Oftalmologo;
import com.example.lab3.entity.Paciente;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente,Integer>{
    List<Paciente> findByClinica_id(int clinica_id);
}
