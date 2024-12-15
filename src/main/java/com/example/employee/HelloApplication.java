package com.example.employee;

import com.example.employee.Utils.MyConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file and assign it to 'root'
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/employee/Add-employee.fxml"));
            Parent root = fxmlLoader.load();  // Load the FXML and assign it to 'root'

            // Set the title and scene for the stage
            primaryStage.setTitle("Page de Connexion");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();  // Handle loading errors
            System.out.println("Erreur lors du chargement du FXML.");
        }
    }

    public static void main(String[] args) {
        // Check the database connection before launching the app
        MyConnection myConnection = MyConnection.getInstance();
        if (myConnection.getCnx() != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("La connexion a échoué.");
        }

        // Launch the JavaFX application
        launch(args);
    }
}
