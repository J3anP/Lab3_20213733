package com.example.lab3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
@Entity
@Table(name="oftalmologo")
public class Oftalmologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id", nullable = false)
    private int id;

    @Column (name="nombre", nullable = false)
    private String nombre;

    @Column (name ="correo", nullable = false)
    private String correo;


    @ManyToOne
    @JoinColumn(name = "clinica_id")
    private Clinica clinica;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }
}
