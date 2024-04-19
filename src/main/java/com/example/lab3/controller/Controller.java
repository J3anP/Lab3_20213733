package com.example.lab3.controller;

import com.example.lab3.entity.*;
import com.example.lab3.repository.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
public class Controller {
    final ClinicaRepository clinicaRepository;

final OftalmologoRepository oftalmologoRepository;

final PacienteRepository pacienteRepository;


    public Controller(ClinicaRepository clinicaRepository, OftalmologoRepository oftalmologoRepository, PacienteRepository pacienteRepository){
        this.clinicaRepository = clinicaRepository;
        this.oftalmologoRepository = oftalmologoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/listClinica")
    public String list(Model model) {

        model.addAttribute("listaClinicas", clinicaRepository.findAll());
        return "clinica/listaClinicas";
    }

    @GetMapping("/listOftalClinica")
    public String listOftalClinica(@RequestParam("clinica_id") int clinica_id, Model model) {
        List<Oftalmologo> listOftalClinica= oftalmologoRepository.buscarPorClinicaId(clinica_id);
        model.addAttribute("listOftalClinica", listOftalClinica);

        return "clinica/listaOftalClinica";
    }


}
