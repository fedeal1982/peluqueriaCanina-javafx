package com.mycompany.pelucanina.controllers;

import com.mycompany.pelucanina.logica.Controladora;
import com.mycompany.pelucanina.logica.Mascota;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VerDatosController {

    @FXML private TableView<Mascota> tablaMascotas;
    @FXML private TableColumn<Mascota, Integer> colNumCliente;
    @FXML private TableColumn<Mascota, String> colNombre;
    @FXML private TableColumn<Mascota, String> colRaza;
    @FXML private TableColumn<Mascota, String> colColor;
    @FXML private TableColumn<Mascota, String> colAlergico;
    @FXML private TableColumn<Mascota, String> colAtencionEspecial;
    @FXML private TableColumn<Mascota, String> colNombreDuenio;
    @FXML private TableColumn<Mascota, String> colCelDuenio;
    @FXML private TableColumn<Mascota, String> colDireccion;
    @FXML private TableColumn<Mascota, String> colCodigoPostal;

    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnActualizar;
    @FXML private Button btnCerrar;
    @FXML private Label lblTotal;

    private Controladora control;
    private ObservableList<Mascota> listaMascotas;

    @FXML
    public void initialize() {
        control = new Controladora();
        configurarTabla();
        cargarDatos();
        configurarListeners();
    }

    private void configurarTabla() {
        // Configurar columnas con PropertyValueFactory
        colNumCliente.setCellValueFactory(new PropertyValueFactory<>("numCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colAlergico.setCellValueFactory(new PropertyValueFactory<>("alergico"));
        colAtencionEspecial.setCellValueFactory(new PropertyValueFactory<>("atencionEspecial"));

        // Columnas que requieren acceso a objetos relacionados
        colRaza.setCellValueFactory(cellData -> {
            if (cellData.getValue().getRaza() != null) {
                return new SimpleStringProperty(cellData.getValue().getRaza().getNombreRaza());
            }
            return new SimpleStringProperty("Sin raza");
        });

        colNombreDuenio.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUnduenio() != null) {
                return new SimpleStringProperty(cellData.getValue().getUnduenio().getNombre());
            }
            return new SimpleStringProperty("-");
        });

        colCelDuenio.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUnduenio() != null) {
                return new SimpleStringProperty(cellData.getValue().getUnduenio().getCelDuenio());
            }
            return new SimpleStringProperty("-");
        });

        colDireccion.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUnduenio() != null) {
                return new SimpleStringProperty(cellData.getValue().getUnduenio().getDireccion());
            }
            return new SimpleStringProperty("-");
        });

        colCodigoPostal.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUnduenio() != null) {
                return new SimpleStringProperty(cellData.getValue().getUnduenio().getCodigoPostal());
            }
            return new SimpleStringProperty("-");
        });
    }

    private void configurarListeners() {
        // Listener para habilitar/deshabilitar botones según la selección
        tablaMascotas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean seleccionado = newSelection != null;
                    btnEditar.setDisable(!seleccionado);
                    btnEliminar.setDisable(!seleccionado);
                }
        );
    }

    private void cargarDatos() {
        try {
            List<Mascota> mascotas = control.traerMascotas();
            listaMascotas = FXCollections.observableArrayList(mascotas);
            tablaMascotas.setItems(listaMascotas);

            // Actualizar contador
            lblTotal.setText("Total de mascotas: " + mascotas.size());

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleActualizar() {
        cargarDatos();
        mostrarAlerta("Actualizado", "Los datos se han actualizado correctamente.",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleEditar() {
        Mascota seleccionada = tablaMascotas.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modificar-datos.fxml"));
                Parent root = loader.load();

                // Pasar la mascota al controlador de modificación
                ModificarDatosController controller = loader.getController();
                controller.cargarDatos(seleccionada);

                Stage stage = new Stage();
                stage.setTitle("Modificar Datos - " + seleccionada.getNombre());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Recargar datos después de cerrar la ventana
                cargarDatos();

            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo abrir la ventana de edición: " + e.getMessage(),
                        Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEliminar() {
        Mascota seleccionada = tablaMascotas.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Eliminar mascota?");
            confirmacion.setContentText(
                    "¿Está seguro que desea eliminar a " + seleccionada.getNombre() + "?\n\n" +
                            "La mascota se moverá a la papelera y podrá ser restaurada posteriormente."
            );

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    control.borrarMascota(seleccionada.getNumCliente());
                    mostrarAlerta("Eliminado",
                            "La mascota ha sido movida a la papelera correctamente.",
                            Alert.AlertType.INFORMATION);
                    cargarDatos();
                } catch (Exception e) {
                    mostrarAlerta("Error", "No se pudo eliminar la mascota: " + e.getMessage(),
                            Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
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