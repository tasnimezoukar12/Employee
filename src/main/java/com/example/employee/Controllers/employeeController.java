package com.example.employee.Controllers;

import com.example.employee.Models.employee;
import com.example.employee.Utils.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.net.URL;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.ByteArrayInputStream;
import java.util.ResourceBundle;

public class employeeController implements Initializable {

    @FXML
    private TableView<employee> employeeTable;
    @FXML
    private TableColumn<employee, String> colEmployeeId;
    @FXML
    private TableColumn<employee, String> colFirstName;
    @FXML
    private TableColumn<employee, String> colLastName;
    @FXML
    private TableColumn<employee, String> colGender;
    @FXML
    private TableColumn<employee, String> colPhone;
    @FXML
    private TableColumn<employee, String> colPosition;
    @FXML
    private TableColumn<employee, String> colDateMember;

    @FXML
    private TextField tfEmployeeId;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfGender;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfPosition;

    @FXML
    private Button addEmployee;
    @FXML
    private Button updateEmployee;
    @FXML
    private Button deleteEmployee;
    @FXML
    private Button addEmployee_importBtn;  // Button to import image

    @FXML
    private ImageView imgView;  // ImageView to display the selected image

    private Connection connection;

    private byte[] imageData;  // Variable to hold the image data

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (tfEmployeeId == null || tfFirstName == null || tfLastName == null || tfGender == null || tfPhone == null || tfPosition == null) {
            showAlert(Alert.AlertType.ERROR, "Initialisation Error", "One or more TextFields are not initialized.");
        }

        // Set the column cell value factory to the corresponding fields of Employee
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colDateMember.setCellValueFactory(new PropertyValueFactory<>("dateMember"));

        connection = MyConnection.getInstance().getCnx();
        loadEmployees();

        // Add event handler to import image
        addEmployee_importBtn.setOnAction(this::importImage);

        employeeTable.setOnMouseClicked(event -> {
            employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                tfEmployeeId.setText(selectedEmployee.getEmployeeId());
                tfFirstName.setText(selectedEmployee.getFirstName());
                tfLastName.setText(selectedEmployee.getLastName());
                tfGender.setText(selectedEmployee.getGender());
                tfPhone.setText(selectedEmployee.getPhone());
                tfPosition.setText(selectedEmployee.getPosition());
                // Display the image from the database
                if (selectedEmployee.getImageData() != null) {
                    Image image = new Image(new ByteArrayInputStream(selectedEmployee.getImageData()));
                    imgView.setImage(image);
                }
            }
        });

        addEmployee.setOnAction(this::add);
        updateEmployee.setOnAction(this::update);
        deleteEmployee.setOnAction(this::delete);
    }

    private void loadEmployees() {
        ObservableList<employee> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(new employee(
                        rs.getString("employeeId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        rs.getString("dateMember"),
                        rs.getBytes("imageData")  // Retrieve the image data as byte[]
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des employés : " + e.getMessage());
        }
        employeeTable.setItems(employees);
    }

    private void add(ActionEvent event) {
        String employeeId = tfEmployeeId.getText();
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String gender = tfGender.getText();
        String phone = tfPhone.getText();
        String position = tfPosition.getText();
        String dateMember = LocalDate.now().toString();

        String query = "INSERT INTO employee (employeeId, firstName, lastName, gender, phone, position, dateMember, imageData) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, gender);
            pstmt.setString(5, phone);
            pstmt.setString(6, position);
            pstmt.setString(7, dateMember);
            pstmt.setBytes(8, imageData != null ? imageData : new byte[0]);  // Check if image is null
            pstmt.executeUpdate();
            loadEmployees();
            clearFields();
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Employé ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'employé : " + e.getMessage());
        }
    }

    private void update(ActionEvent event) {
        employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            String currentDate = LocalDate.now().toString();

            String query = "UPDATE employee SET firstName = ?, lastName = ?, gender = ?, phone = ?, position = ?, dateMember = ?, imageData = ? WHERE employeeId = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, tfFirstName.getText());
                pstmt.setString(2, tfLastName.getText());
                pstmt.setString(3, tfGender.getText());
                pstmt.setString(4, tfPhone.getText());
                pstmt.setString(5, tfPosition.getText());
                pstmt.setString(6, currentDate);
                pstmt.setBytes(7, imageData != null ? imageData : new byte[0]);  // Check if image is null
                pstmt.setString(8, selectedEmployee.getEmployeeId());

                pstmt.executeUpdate();
                loadEmployees();
                clearFields();
                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Employé mis à jour avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour de l'employé : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Employé sélectionné", "Veuillez sélectionner un employé à mettre à jour.");
        }
    }

    private void delete(ActionEvent event) {
        employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            String query = "DELETE FROM employee WHERE employeeId = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, selectedEmployee.getEmployeeId());
                pstmt.executeUpdate();
                loadEmployees();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé supprimé avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression de l'employé : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun Employé sélectionné", "Veuillez sélectionner un employé à supprimer.");
        }
    }

    private void importImage(ActionEvent event) {
        // Ouvrir le FileChooser pour sélectionner l'image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Lire l'image en bytes
            try {
                imageData = Files.readAllBytes(selectedFile.toPath());
                // Afficher l'image dans l'ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imgView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'importation de l'image.");
            }
        }
    }

    private void clearFields() {
        tfEmployeeId.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfGender.clear();
        tfPhone.clear();
        tfPosition.clear();
        imgView.setImage(null);  // Clear the image view when clearing fields
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
