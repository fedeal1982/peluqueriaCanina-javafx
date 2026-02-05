package com.mycompany.pelucanina.controllers;

import com.mycompany.pelucanina.logica.Controladora;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PrincipalController {

    @FXML private Button btnCargarDatos;
    @FXML private Button btnVerDatos;
    @FXML private Button btnPapelera;
    @FXML private Button btnSalir;

    private Controladora control;

    @FXML
    public void initialize() {
        control = new Controladora();
        // Inicializar razas si no existen
        control.inicializarRazas();
    }

    @FXML
    private void handleCargarDatos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cargar-datos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cargar Datos - Nueva Mascota");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de cargar datos: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVerDatos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ver-datos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ver Datos - Lista de Mascotas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de ver datos: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePapelera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/papelera.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Papelera de Reciclaje");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la papelera: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalir() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar salida");
        confirmacion.setHeaderText("¿Está seguro que desea salir?");
        confirmacion.setContentText("La aplicación se cerrará.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}