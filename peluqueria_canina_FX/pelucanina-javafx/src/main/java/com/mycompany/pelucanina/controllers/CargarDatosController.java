package com.mycompany.pelucanina.controllers;

import com.mycompany.pelucanina.logica.Controladora;
import com.mycompany.pelucanina.logica.Raza;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CargarDatosController {

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
    @FXML private Button btnLimpiar;
    @FXML private Button btnCancelar;

    private Controladora control;

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

        if (!razas.isEmpty()) {
            cmbRaza.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleGuardar() {
        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }

        try {
            // Obtener datos de la mascota
            String nombreMasco = txtNombre.getText().trim();
            int idRaza = cmbRaza.getSelectionModel().getSelectedItem().getIdRaza();
            String color = txtColor.getText().trim();
            String alergico = cmbAlergico.getValue();
            String atenEsp = cmbAtencionEspecial.getValue();
            String observaciones = txtObservaciones.getText().trim();

            // Obtener datos del dueño
            String nombreDuenio = txtNombreDuenio.getText().trim();
            String celDuenio = txtCelDuenio.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String codigoPostal = txtCodigoPostal.getText().trim();

            // Guardar
            control.guardar(nombreMasco, idRaza, color, alergico, atenEsp,
                    nombreDuenio, celDuenio, direccion, codigoPostal, observaciones);

            // Mensaje de éxito
            mostrarAlerta("Éxito", "La mascota se guardó correctamente.", Alert.AlertType.INFORMATION);

            // Limpiar formulario
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar la mascota: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
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

    private void limpiarCampos() {
        txtNombre.clear();
        txtColor.clear();
        txtObservaciones.clear();
        txtNombreDuenio.clear();
        txtCelDuenio.clear();
        txtDireccion.clear();
        txtCodigoPostal.clear();

        cmbAlergico.getSelectionModel().selectFirst();
        cmbAtencionEspecial.getSelectionModel().selectFirst();

        if (!cmbRaza.getItems().isEmpty()) {
            cmbRaza.getSelectionModel().selectFirst();
        }

        txtNombre.requestFocus();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}