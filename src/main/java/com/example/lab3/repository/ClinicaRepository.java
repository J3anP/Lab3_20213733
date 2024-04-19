package com.example.lab3.repository;
import com.example.lab3.entity.Clinica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface ClinicaRepository extends JpaRepository<Clinica,Integer> {



}
