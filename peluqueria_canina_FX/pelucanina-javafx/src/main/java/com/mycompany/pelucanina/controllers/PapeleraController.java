package com.mycompany.pelucanina.controllers;

import com.mycompany.pelucanina.logica.Controladora;
import com.mycompany.pelucanina.logica.MascotaEliminada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PapeleraController {

    @FXML private TableView<MascotaEliminada> tablaPapelera;
    @FXML private TableColumn<MascotaEliminada, String> colNombre;
    @FXML private TableColumn<MascotaEliminada, String> colRaza;
    @FXML private TableColumn<MascotaEliminada, String> colDuenio;
    @FXML private TableColumn<MascotaEliminada, String> colFechaEliminacion;
    @FXML private Button btnRestaurar;
    @FXML private Button btnEliminarPermanente;
    @FXML private Button btnVaciarPapelera;
    @FXML private Button btnCerrar;
    @FXML private Label lblTotal;

    private Controladora control;
    private ObservableList<MascotaEliminada> listaMascotas;

    @FXML
    public void initialize() {
        control = new Controladora();
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        // Configurar columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("nombreRaza"));
        colDuenio.setCellValueFactory(new PropertyValueFactory<>("nombreDuenio"));

        // Formatear fecha de eliminación
        colFechaEliminacion.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFechaEliminacion() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getFechaEliminacion().format(formatter)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });

        // Listener para habilitar/deshabilitar botones
        tablaPapelera.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean seleccionado = newSelection != null;
                    btnRestaurar.setDisable(!seleccionado);
                    btnEliminarPermanente.setDisable(!seleccionado);
                }
        );
    }

    private void cargarDatos() {
        try {
            List<MascotaEliminada> mascotas = control.traerMascotasEliminadas();
            listaMascotas = FXCollections.observableArrayList(mascotas);
            tablaPapelera.setItems(listaMascotas);

            // Actualizar contador
            lblTotal.setText("Total de elementos: " + mascotas.size());

            // Deshabilitar botón de vaciar si está vacía
            btnVaciarPapelera.setDisable(mascotas.isEmpty());
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRestaurar() {
        MascotaEliminada seleccionada = tablaPapelera.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar restauración");
            confirmacion.setHeaderText("¿Restaurar mascota?");
            confirmacion.setContentText(
                    "¿Está seguro que desea restaurar a " + seleccionada.getNombre() + "?\n" +
                            "Se creará un nuevo registro en el sistema."
            );

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    control.restaurarMascota(seleccionada.getId());
                    mostrarAlerta(
                            "Restauración exitosa",
                            "La mascota ha sido restaurada correctamente.",
                            Alert.AlertType.INFORMATION
                    );
                    cargarDatos();
                } catch (Exception e) {
                    mostrarAlerta(
                            "Error",
                            "No se pudo restaurar la mascota: " + e.getMessage(),
                            Alert.AlertType.ERROR
                    );
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleEliminarPermanente() {
        MascotaEliminada seleccionada = tablaPapelera.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.WARNING);
            confirmacion.setTitle("Confirmar eliminación permanente");
            confirmacion.setHeaderText("¡ATENCIÓN! Esta acción es irreversible");
            confirmacion.setContentText(
                    "¿Está seguro que desea eliminar permanentemente a " +
                            seleccionada.getNombre() + "?\n" +
                            "Esta acción NO se puede deshacer."
            );

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    control.eliminarPermanentemente(seleccionada.getId());
                    mostrarAlerta(
                            "Eliminación exitosa",
                            "El registro ha sido eliminado permanentemente.",
                            Alert.AlertType.INFORMATION
                    );
                    cargarDatos();
                } catch (Exception e) {
                    mostrarAlerta(
                            "Error",
                            "No se pudo eliminar el registro: " + e.getMessage(),
                            Alert.AlertType.ERROR
                    );
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleVaciarPapelera() {
        if (listaMascotas.isEmpty()) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.WARNING);
        confirmacion.setTitle("Confirmar vaciado de papelera");
        confirmacion.setHeaderText("¡ATENCIÓN! Esta acción es irreversible");
        confirmacion.setContentText(
                "¿Está seguro que desea vaciar completamente la papelera?\n" +
                        "Se eliminarán permanentemente " + listaMascotas.size() + " registros.\n" +
                        "Esta acción NO se puede deshacer."
        );

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                control.vaciarPapelera();
                mostrarAlerta(
                        "Papelera vaciada",
                        "La papelera ha sido vaciada completamente.",
                        Alert.AlertType.INFORMATION
                );
                cargarDatos();
            } catch (Exception e) {
                mostrarAlerta(
                        "Error",
                        "No se pudo vaciar la papelera: " + e.getMessage(),
                        Alert.AlertType.ERROR
                );
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}