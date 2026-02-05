package com.mycompany.pelucanina.logica;

import com.mycompany.pelucanina.persistencia.ControladoraPersistencia;
import java.util.List;

public class Controladora {

    private final ControladoraPersistencia controlPersis;

    public Controladora() {
        this.controlPersis = new ControladoraPersistencia();
    }

    // ========== OPERACIONES DE MASCOTA ==========

    public void guardar(String nombreMasco, int idRaza, String color, String alergico,
                        String atenEsp, String nombreDuenio, String celDuenio,
                        String direccion, String codigoPostal, String observaciones) {

        Duenio duenio = new Duenio();
        duenio.setNombre(nombreDuenio);
        duenio.setCelDuenio(celDuenio);
        duenio.setDireccion(direccion);
        duenio.setCodigoPostal(codigoPostal);

        Raza raza = controlPersis.traerRaza(idRaza);

        Mascota masco = new Mascota();
        masco.setNombre(nombreMasco);
        masco.setRaza(raza);
        masco.setColor(color);
        masco.setAlergico(alergico);
        masco.setAtencionEspecial(atenEsp);
        masco.setObservaciones(observaciones);
        masco.setUnduenio(duenio);

        controlPersis.guardar(duenio, masco);
    }

    public List<Mascota> traerMascotas() {
        return controlPersis.traerMascotas();
    }

    public Mascota traerMascota(int numCliente) {
        return controlPersis.traerMascota(numCliente);
    }

    public void modificarMascota(Mascota masco, String nombreMasco, int idRaza,
                                 String color, String observaciones, String alergico,
                                 String atenEsp, String nombreDuenio, String celDuenio,
                                 String direccion, String codigoPostal) {

        Raza raza = controlPersis.traerRaza(idRaza);

        masco.setNombre(nombreMasco);
        masco.setRaza(raza);
        masco.setColor(color);
        masco.setObservaciones(observaciones);
        masco.setAlergico(alergico);
        masco.setAtencionEspecial(atenEsp);

        controlPersis.modificarMascota(masco);

        Duenio dueno = buscarDuenio(masco.getUnduenio().getIdDuenio());
        dueno.setNombre(nombreDuenio);
        dueno.setCelDuenio(celDuenio);
        dueno.setDireccion(direccion);
        dueno.setCodigoPostal(codigoPostal);

        modificarDuenio(dueno);
    }

    /**
     * Elimina una mascota moviéndola a la papelera
     */
    public void borrarMascota(int numCliente) {
        // Primero obtenemos la mascota
        Mascota mascota = controlPersis.traerMascota(numCliente);

        if (mascota != null) {
            // Creamos una copia en la papelera
            MascotaEliminada mascotaEliminada = new MascotaEliminada(mascota);
            controlPersis.guardarEnPapelera(mascotaEliminada);

            // Luego eliminamos la mascota original
            controlPersis.borrarMascota(numCliente);
        }
    }

    // ========== OPERACIONES DE RAZAS ==========

    public void crearRaza(String nombreRaza, String descripcion) {
        Raza raza = new Raza();
        raza.setNombreRaza(nombreRaza);
        raza.setDescripcion(descripcion);
        controlPersis.guardarRaza(raza);
    }

    public List<Raza> traerRazas() {
        return controlPersis.traerRazas();
    }

    public void inicializarRazas() {
        List<Raza> razasExistentes = traerRazas();
        if (razasExistentes.isEmpty()) {
            crearRaza("Labrador", "Perro grande, amigable y enérgico");
            crearRaza("Golden Retriever", "Perro grande, gentil y confiable");
            crearRaza("Pastor Alemán", "Perro grande, inteligente y versátil");
            crearRaza("Bulldog Francés", "Perro pequeño, adaptable y juguetón");
            crearRaza("Chihuahua", "Perro muy pequeño, alerta y vivaz");
            crearRaza("Poodle", "Perro inteligente, activo y elegante");
            crearRaza("Beagle", "Perro mediano, amigable y curioso");
            crearRaza("Rottweiler", "Perro grande, robusto y confiado");
            crearRaza("Yorkshire Terrier", "Perro pequeño, valiente y enérgico");
            crearRaza("Mestizo", "Mezcla de razas");
        }
    }

    // ========== OPERACIONES DE PAPELERA ==========

    public List<MascotaEliminada> traerMascotasEliminadas() {
        return controlPersis.traerMascotasEliminadas();
    }

    /**
     * Restaura una mascota desde la papelera
     */
    public void restaurarMascota(Long idEliminada) {
        MascotaEliminada mascotaEliminada = controlPersis.traerMascotaEliminada(idEliminada);

        if (mascotaEliminada != null) {
            // Recrear el dueño
            Duenio duenio = new Duenio();
            duenio.setNombre(mascotaEliminada.getNombreDuenio());
            duenio.setCelDuenio(mascotaEliminada.getCelDuenio());
            duenio.setDireccion(mascotaEliminada.getDireccion());
            duenio.setCodigoPostal(mascotaEliminada.getCodigoPostal());

            // Buscar la raza por nombre
            Raza raza = buscarRazaPorNombre(mascotaEliminada.getNombreRaza());

            // Recrear la mascota
            Mascota mascota = new Mascota();
            mascota.setNombre(mascotaEliminada.getNombre());
            mascota.setColor(mascotaEliminada.getColor());
            mascota.setAlergico(mascotaEliminada.getAlergico());
            mascota.setAtencionEspecial(mascotaEliminada.getAtencionEspecial());
            mascota.setObservaciones(mascotaEliminada.getObservaciones());
            mascota.setRaza(raza);
            mascota.setUnduenio(duenio);

            // Guardar la mascota restaurada
            controlPersis.guardar(duenio, mascota);

            // Eliminar de la papelera
            controlPersis.eliminarDePapelera(idEliminada);
        }
    }

    /**
     * Elimina permanentemente una mascota de la papelera
     */
    public void eliminarPermanentemente(Long idEliminada) {
        controlPersis.eliminarDePapelera(idEliminada);
    }

    /**
     * Vacía toda la papelera
     */
    public void vaciarPapelera() {
        controlPersis.vaciarPapelera();
    }

    // ========== MÉTODOS PRIVADOS ==========

    private Duenio buscarDuenio(int idDuenio) {
        return controlPersis.traerDuenio(idDuenio);
    }

    private void modificarDuenio(Duenio dueno) {
        controlPersis.modificarDuenio(dueno);
    }

    private Raza buscarRazaPorNombre(String nombreRaza) {
        List<Raza> razas = traerRazas();
        return razas.stream()
                .filter(r -> r.getNombreRaza().equalsIgnoreCase(nombreRaza))
                .findFirst()
                .orElse(null);
    }
}