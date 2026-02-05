package com.mycompany.pelucanina.persistencia;

import com.mycompany.pelucanina.logica.Duenio;
import com.mycompany.pelucanina.logica.Mascota;
import com.mycompany.pelucanina.logica.MascotaEliminada;
import com.mycompany.pelucanina.logica.Raza;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladoraPersistencia {

    private final DuenioJpaController duenioJpa;
    private final MascotaJpaController mascoJpa;
    private final RazaJpaController razaJpa;
    private final MascotaEliminadaJpaController papeleraJpa;

    public ControladoraPersistencia() {
        this.duenioJpa = new DuenioJpaController();
        this.mascoJpa = new MascotaJpaController();
        this.razaJpa = new RazaJpaController();
        this.papeleraJpa = new MascotaEliminadaJpaController();
    }

    // ========== OPERACIONES DE MASCOTA ==========

    public void guardar(Duenio duenio, Mascota masco) {
        duenioJpa.create(duenio);
        mascoJpa.create(masco);
    }

    public void guardarMascota(Mascota masco) {
        mascoJpa.create(masco);
    }

    public List<Mascota> traerMascotas() {
        return mascoJpa.findMascotaEntities();
    }

    public Mascota traerMascota(int numCliente) {
        return mascoJpa.findMascota(numCliente);
    }

    public void modificarMascota(Mascota masco) {
        try {
            mascoJpa.edit(masco);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName())
                    .log(Level.SEVERE, "Error al modificar mascota", ex);
        }
    }

    public void borrarMascota(int numCliente) {
        try {
            mascoJpa.destroy(numCliente);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName())
                    .log(Level.SEVERE, "Error al borrar mascota", ex);
        }
    }

    // ========== OPERACIONES DE DUEÑO ==========

    public Duenio traerDuenio(int idDuenio) {
        return duenioJpa.findDuenio(idDuenio);
    }

    public void modificarDuenio(Duenio dueno) {
        try {
            duenioJpa.edit(dueno);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName())
                    .log(Level.SEVERE, "Error al modificar dueño", ex);
        }
    }

    // ========== OPERACIONES DE RAZA ==========

    public void guardarRaza(Raza raza) {
        razaJpa.create(raza);
    }

    public List<Raza> traerRazas() {
        return razaJpa.findRazaEntities();
    }

    public Raza traerRaza(int idRaza) {
        return razaJpa.findRaza(idRaza);
    }

    // ========== OPERACIONES DE PAPELERA ==========

    /**
     * Guarda una mascota en la papelera
     */
    public void guardarEnPapelera(MascotaEliminada mascotaEliminada) {
        papeleraJpa.create(mascotaEliminada);
    }

    /**
     * Trae todas las mascotas eliminadas
     */
    public List<MascotaEliminada> traerMascotasEliminadas() {
        return papeleraJpa.findMascotaEliminadaEntities();
    }

    /**
     * Trae una mascota eliminada específica
     */
    public MascotaEliminada traerMascotaEliminada(Long id) {
        return papeleraJpa.findMascotaEliminada(id);
    }

    /**
     * Elimina permanentemente una mascota de la papelera
     */
    public void eliminarDePapelera(Long id) {
        try {
            papeleraJpa.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName())
                    .log(Level.SEVERE, "Error al eliminar de papelera", ex);
        }
    }

    /**
     * Vacía toda la papelera
     */
    public void vaciarPapelera() {
        List<MascotaEliminada> mascotas = traerMascotasEliminadas();
        for (MascotaEliminada mascota : mascotas) {
            eliminarDePapelera(mascota.getId());
        }
    }
}