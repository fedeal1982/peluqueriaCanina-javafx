package com.mycompany.pelucanina.logica;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "razas")
public class Raza implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRaza;

    @Column(name = "nombre_raza", nullable = false, length = 100)
    private String nombreRaza;

    @Column(length = 255)
    private String descripcion;

    // Constructores
    public Raza() {
    }

    public Raza(String nombreRaza, String descripcion) {
        this.nombreRaza = nombreRaza;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getIdRaza() {
        return idRaza;
    }

    public void setIdRaza(Integer idRaza) {
        this.idRaza = idRaza;
    }

    public String getNombreRaza() {
        return nombreRaza;
    }

    public void setNombreRaza(String nombreRaza) {
        this.nombreRaza = nombreRaza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Para mostrar en ComboBox
    @Override
    public String toString() {
        return this.nombreRaza;
    }
}