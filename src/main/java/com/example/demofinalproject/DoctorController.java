package com.example.demofinalproject;

import Services.ClientConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.LinkPermission;
import java.util.Objects;

public class DoctorController {

    //    Doctor registration fields
    public TextField newUsernameField;
    public PasswordField newPasswordField;
    public TextField occupationField;
    public ChoiceBox genderDropdown;
    public TextField ageField;
    public TextField specialization;


    // Doctor scene fields
    public TextField searchPatientName;
    public ChoiceBox selectDoctorAction;
    public Label patientInfoLabel;
    public TextArea patientInfoTextField;
    public TextField searchPatientAge;
    @FXML
    public Label helloLabel;
    public Label badCreds;


    //    Socket connection
    ClientConnection doctorConnection = ClientConnection.getInstance();


    //Doctor login fields
    public TextField usernameField;
    public PasswordField passwordField;


    // Method that handles the Doctor / Admin Login
    public void handleLoginAction(ActionEvent actionEvent) {
        doctorConnection.socketConnect();

        if (usernameField.getText().equalsIgnoreCase("admin")) {
            String commandToPass = "adm_login@" + usernameField.getText() + "&" + passwordField.getText();
            if (doctorConnection.passToServer(commandToPass).equalsIgnoreCase("true")) {
                switchScene(actionEvent, "admin_scene.fxml");
            } else {
                switchScene(actionEvent, "doctor_login_scene.fxml");
            }
        } else {
            String commandToPass = "d_login@" + usernameField.getText() + "&" + passwordField.getText();
            if (doctorConnection.passToServer(commandToPass).equalsIgnoreCase("true")) {
                switchScene(actionEvent, "doctor_scene.fxml");
            } else {
                badCreds.setVisible(true);
//                switchScene(actionEvent, "doctor_login_scene.fxml");
            }
        }


    }

    public void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile))); // Exception can be thrown here
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception
        }
    }


    //Admin registration buttons
    public void handleAdminRegisterAction(ActionEvent actionEvent) {
        System.out.println("Doctor registered");
        doctorConnection.socketConnect();
        String commandToPass = "d_reg@" + newUsernameField.getText() + "&" + newPasswordField.getText() + "&" + occupationField.getText() + "&" + genderDropdown.getValue() + "&" + ageField.getText() + "&" + specialization.getText();
        if (doctorConnection.passToServer(commandToPass).equalsIgnoreCase("true")) {
            switchScene(actionEvent, "doctor_scene.fxml");
        } else {
            switchScene(actionEvent, "admin_scene.fxml");
        }
    }


    //doctor scene buttons
    public void onActionSelectPatient(ActionEvent actionEvent) {
        doctorConnection.socketConnect();
        String commandToPass = "p_find@" + searchPatientName.getText() + "&" + searchPatientAge.getText();
        String serverResponse = doctorConnection.passToServer(commandToPass);
        if (serverResponse.equalsIgnoreCase("false")) {
            patientInfoLabel.setText("There is no patient in the base with provided name and age. Try again");
        } else {
            String[] infoTmp = serverResponse.split("&");
            String lineToShow = "Patient name: " + infoTmp[0] + "\n" + "Age: " + infoTmp[1] + "\n" + "Reason for visit: " + "\n" + infoTmp[2] + "\n" + "Patient meds: " + infoTmp[3];
            patientInfoLabel.setText(lineToShow);
            System.out.println(serverResponse);
        }
    }

    public void handleUpdateInfo(ActionEvent actionEvent) {
        doctorConnection.socketConnect();
        if (doctorConnection.passToServer("_isPatientChosen@_&").equalsIgnoreCase("true")) {
            String commandToPass = "p_infoUpdate@" + selectDoctorAction.getValue() + "&" + patientInfoTextField.getText();
            if (doctorConnection.passToServer(commandToPass).equalsIgnoreCase("true")) {
                patientInfoTextField.setText("Patient info was updated");
            } else {
                patientInfoTextField.setText("Setting info occurred, try again!");
            }
        } else {
            patientInfoLabel.setText("Choose the patient first");
        }
    }

    public void handleLogoutAction(ActionEvent actionEvent) {
        doctorConnection.socketConnect();
        doctorConnection.passToServer("_logout@doctor&");
        switchScene(actionEvent, "welcome_scene.fxml");
    }
}
