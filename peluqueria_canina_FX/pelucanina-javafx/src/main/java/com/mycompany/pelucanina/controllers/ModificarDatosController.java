package com.mycompany.pelucanina.controllers;

import com.mycompany.pelucanina.logica.Controladora;
import com.mycompany.pelucanina.logica.Mascota;
import com.mycompany.pelucanina.logica.Raza;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class ModificarDatosController {

    // Campos de Mascota
    @FXML private TextField txtNombre;
    @FXML private ComboBox<Raza> cmbRaza;
    @FXML private TextField txtColor;
    @FXML private ComboBox<String> cmbAlergico;
    @FXML private ComboBox<String> cmbAtencionEspecial;
    @FXML private TextArea txtObservaciones;

    // Campos de Dueño
    @FXML private TextField txtNombreDuenio;
    @FXML private TextField txtCelDuenio;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCodigoPostal;

    // Botones
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Controladora control;
    private Mascota mascotaActual;

    @FXML
    public void initialize() {
        control = new Controladora();
        configurarComboBoxes();
        cargarRazas();
    }

    private void configurarComboBoxes() {
        // ComboBox Alérgico
        cmbAlergico.setItems(FXCollections.observableArrayList("-", "SI", "NO"));
        cmbAlergico.getSelectionModel().selectFirst();

        // ComboBox Atención Especial
        cmbAtencionEspecial.setItems(FXCollections.observableArrayList("-", "SI", "NO"));
        cmbAtencionEspecial.getSelectionModel().selectFirst();
    }

    private void cargarRazas() {
        List<Raza> razas = control.traerRazas();
        cmbRaza.setItems(FXCollections.observableArrayList(razas));
    }

    /**
     * Método público para cargar los datos de una mascota existente
     */
    public void cargarDatos(Mascota mascota) {
        this.mascotaActual = mascota;

        if (mascota != null) {
            // Cargar datos de la mascota
            txtNombre.setText(mascota.getNombre());
            txtColor.setText(mascota.getColor());
            txtObservaciones.setText(mascota.getObservaciones());

            // Seleccionar alérgico
            seleccionarEnCombo(cmbAlergico, mascota.getAlergico());

            // Seleccionar atención especial
            seleccionarEnCombo(cmbAtencionEspecial, mascota.getAtencionEspecial());

            // Seleccionar raza
            if (mascota.getRaza() != null) {
                for (Raza raza : cmbRaza.getItems()) {
                    if (raza.getIdRaza().equals(mascota.getRaza().getIdRaza())) {
                        cmbRaza.getSelectionModel().select(raza);
                        break;
                    }
                }
            }

            // Cargar datos del dueño
            if (mascota.getUnduenio() != null) {
                txtNombreDuenio.setText(mascota.getUnduenio().getNombre());
                txtCelDuenio.setText(mascota.getUnduenio().getCelDuenio());
                txtDireccion.setText(mascota.getUnduenio().getDireccion());
                txtCodigoPostal.setText(mascota.getUnduenio().getCodigoPostal());
            }
        }
    }

    private void seleccionarEnCombo(ComboBox<String> combo, String valor) {
        if (valor != null) {
            if (valor.equalsIgnoreCase("SI")) {
                combo.getSelectionModel().select("SI");
            } else if (valor.equalsIgnoreCase("NO")) {
                combo.getSelectionModel().select("NO");
            } else {
                combo.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    private void handleGuardar() {
        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }

        try {
            // Obtener datos del formulario
            String nombreMasco = txtNombre.getText().trim();
            int idRaza = cmbRaza.getSelectionModel().getSelectedItem().getIdRaza();
            String color = txtColor.getText().trim();
            String observaciones = txtObservaciones.getText().trim();
            String alergico = cmbAlergico.getValue();
            String atenEsp = cmbAtencionEspecial.getValue();

            String nombreDuenio = txtNombreDuenio.getText().trim();
            String celDuenio = txtCelDuenio.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String codigoPostal = txtCodigoPostal.getText().trim();

            // Modificar la mascota
            control.modificarMascota(mascotaActual, nombreMasco, idRaza, color,
                    observaciones, alergico, atenEsp, nombreDuenio,
                    celDuenio, direccion, codigoPostal);

            // Mensaje de éxito
            mostrarAlerta("Éxito", "Los datos se modificaron correctamente.",
                    Alert.AlertType.INFORMATION);

            // Cerrar ventana
            handleCancelar();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron guardar los cambios: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- Nombre de la mascota\n");
        }

        if (cmbRaza.getSelectionModel().getSelectedItem() == null) {
            errores.append("- Raza\n");
        }

        if (txtNombreDuenio.getText().trim().isEmpty()) {
            errores.append("- Nombre del dueño\n");
        }

        if (txtCelDuenio.getText().trim().isEmpty()) {
            errores.append("- Celular del dueño\n");
        }

        if (txtDireccion.getText().trim().isEmpty()) {
            errores.append("- Dirección\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta("Campos Obligatorios",
                    "Por favor complete los siguientes campos:\n\n" + errores.toString(),
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}