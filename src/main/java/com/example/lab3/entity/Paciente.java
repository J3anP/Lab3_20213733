package com.example.lab3.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false)
    private int id;

    @Column (name="nombre", nullable = false)
    private String nombre;

    @Column (name="edad", nullable = false)
    private int edad;

    @Column (name="genero", nullable = false)
    private String genero;

    @Column (name="diagnostico", nullable = false)
    private String diagonostico;

    @Column (name="fecha_cita", nullable = false)
    private String fecha_cita;

    @Column(name="numero_habitacion",nullable = false)
    private int numero_habitacion;

    @ManyToOne
    @JoinColumn(name="oftalmologo_id")
    private Oftalmologo oftalmologo;

    @ManyToOne
    @JoinColumn (name ="clinica_id")
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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDiagonostico() {
        return diagonostico;
    }

    public void setDiagonostico(String diagonostico) {
        this.diagonostico = diagonostico;
    }

    public String getFecha_cita() {
        return fecha_cita;
    }

    public void setFecha_cita(String fecha_cita) {
        this.fecha_cita = fecha_cita;
    }

    public int getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(int numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }

    public Oftalmologo getOftalmologo() {
        return oftalmologo;
    }

    public void setOftalmologo(Oftalmologo oftalmologo) {
        this.oftalmologo = oftalmologo;
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }
}
