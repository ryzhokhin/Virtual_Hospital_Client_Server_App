package com.example.demofinalproject;

import Services.ClientConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.demofinalproject.HelloController;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PatientController {
    
//    Patient Login Fields
    public TextField usernameField;
    public PasswordField passwordField;
    
//    Patient register fields
    public TextField occupationField;
    public ChoiceBox genderComboBox;
    public TextField ageField;
    public TextField reasonForVisitField;
    public TextField usernameField_reg;
    public PasswordField passwordField_reg;


//    Patient scene fields
    public Label setDoctorNotes;
    public Label setDoctorMeds;
    
//    Appointment scene fields
    public Label aptWelcome;
    public Label aptName;
    public TextField patientNameTextField;
    public DatePicker aptDate;
    public TextField aptTime;
    public Button bookApt;
    public Button backToPatient;
    public Label aptConfirmation;
    public Label aptDateLabel;
    public Label aptTimeLabel;

//    Payment scene fields
    public Label pay1;
    public Label pay2;
    public TextField cardHolder;
    public Label pay4;
    public TextField cardNumber;
    public Label pay6;
    public TextField expDate;
    public Label pay8;
    public TextField cardCVV;
    public Button paymentBtn;
    public Label success;
    public Button backToPatientFromPayment;
    public Label badCreds;


    ClientConnection patientConnection = ClientConnection.getInstance();



    
//    Methods to handle the login request 
    public void handleLoginAction(ActionEvent actionEvent) {

        patientConnection.socketConnect();
        String commandToPass = "p_login@"+usernameField.getText()+"&"+passwordField.getText();
        String temp_serverResponse = patientConnection.passToServer(commandToPass);
        if(temp_serverResponse.equalsIgnoreCase("true")){
            switchScene(actionEvent, "patient_scene.fxml");
        }else if(temp_serverResponse.equalsIgnoreCase("false")){
            badCreds.setVisible(true);

        }
    }

    
//Methods to switch the scenes
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
    
    
    
//Method to handle registration request of new patient
    public void handleRegisterAction(ActionEvent actionEvent) {
        patientConnection.socketConnect();
        String commandToPass = "p_reg@"+usernameField_reg.getText()+"&"+passwordField_reg.getText()+"&"+occupationField.getText()+"&"+genderComboBox.getValue()+"&"+ageField.getText()+"&"+reasonForVisitField.getText();
        if(patientConnection.passToServer(commandToPass).equalsIgnoreCase("true")){
            switchScene(actionEvent, "patient_scene.fxml");

        }else{
            switchScene(actionEvent, "patient_scene.fxml");
        }
    }
    

    //   Methods in the patient scene
    public void viewDoctorNotes(ActionEvent actionEvent) {
        patientConnection.socketConnect();
        String serverResponse = patientConnection.passToServer("p_notesReq@_&");
        setDoctorMeds.setVisible(false);
        setDoctorNotes.setVisible(true);
//        if(!serverResponse.equalsIgnoreCase("false")){
//            setDoctorNotes.setText(serverResponse);
//        }else{
//            setDoctorNotes.setText("The doctor has not left you any notes yet");
//        }
        System.out.println("Server response: " + serverResponse);
        setDoctorMeds.setText("");
        setDoctorNotes.setText(serverResponse);
    }


    public void viewMedications(ActionEvent actionEvent) {
        patientConnection.socketConnect();
        String serverResponse = patientConnection.passToServer("p_medsReq@_&");
        setDoctorNotes.setVisible(false);
        setDoctorMeds.setVisible(true);
        System.out.println("Server response: "+ serverResponse);
        setDoctorNotes.setText("");
        String[] medsArr = serverResponse.split(",");
        String medsTmp = "";
        for(String str: medsArr){
            medsTmp = medsTmp + str + "\n";
        }
        String medsFinal = medsTmp.replace("&", "\n");
        System.out.println(medsFinal);
        setDoctorMeds.setText(medsFinal);
    }

    public void makeAppointmentButtonClicked(ActionEvent actionEvent) {
        switchScene(actionEvent, "patient_make_appointment_scene.fxml");
    }

    public void makePaymentButtonClicked(ActionEvent actionEvent) {
        switchScene(actionEvent,"patient_make_payment_scene.fxml");
    }

    public void onActionMakeAppointment(ActionEvent actionEvent) {
        System.out.println(patientNameTextField.getText());
        System.out.println(aptDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println(aptTime.getText());
        patientConnection.socketConnect();
        String commandToPass = "p_makeApt@" + aptDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-" + aptTime.getText()+"&_";
        if(!patientConnection.passToServer(commandToPass).equalsIgnoreCase("true")){
            aptConfirmation.setText("Something went wrong");
        }
        aptWelcome.setVisible(false);
        aptName.setVisible(false);
        patientNameTextField.setVisible(false);
        aptDate.setVisible(false);
        aptTime.setVisible(false);
        bookApt.setVisible(false);
        aptDateLabel.setVisible(false);
        aptTimeLabel.setVisible(false);
        backToPatient.setVisible(true);
        aptConfirmation.setVisible(true);

    }

    public void onActionBackToPatient(ActionEvent actionEvent) {
        switchScene(actionEvent, "patient_scene.fxml");
    }

    public void onActionPayment(ActionEvent actionEvent) {
        patientConnection.socketConnect();
        String commandToPass = "p_payment@"+cardHolder.getText()+"#"+cardNumber.getText()+"#"+cardCVV.getText() + "&_";
        if(!patientConnection.passToServer(commandToPass).equalsIgnoreCase("true")){
            success.setText("Something went wrong");
        }
        pay1.setVisible(false);
        pay2.setVisible(false);
        cardHolder.setVisible(false);
        pay4.setVisible(false);
        cardNumber.setVisible(false);
        pay6.setVisible(false);
        expDate.setVisible(false);
        pay8.setVisible(false);
        cardCVV.setVisible(false);
        paymentBtn.setVisible(false);
        success.setVisible(true);
        backToPatientFromPayment.setVisible(true);

    }

    public void handleLogoutAction(ActionEvent actionEvent) {
        patientConnection.socketConnect();
        patientConnection.passToServer("_logout@patient&");
        switchScene(actionEvent, "welcome_scene.fxml");
    }
}
