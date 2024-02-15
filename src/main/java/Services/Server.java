package Services;

import models.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    private static ArrayList<User> userList = new ArrayList<>();
    static User activeUser;
    static Patient patientDoctorWorksWith;

    /* This method starts a server and handles all the requests on it, to then pass it to the operation classes */
    public static void main(String[] args) {


        userList.add(new Admin("admin", "", "Admin", 99, "root"));
        addPatient("Sam", "realPassword", "urgent", "male", 19, "Cough");
        updatePatientInfo((Patient) searchPatient("sam", 19), "Prescribe Meds", "testing meds");
        updatePatientInfo((Patient) searchPatient("sam", 19), "Prescribe Meds", "testing meds 2");
        userList.add(new Doctor("Andrew", "test", "Male", 45, "Doctor", "doctorPass"));
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started, waiting for the connection...");
            while (true) {
                System.out.println("Waiting for client");
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Client connected, listening");
                    InputStream input = socket.getInputStream();
                    OutputStream output = socket.getOutputStream();
                    Scanner scanner = new Scanner(input);
                    PrintWriter writer = new PrintWriter(output, true);

                    while (scanner.hasNextLine()) {
                        String inputLine = scanner.nextLine(); // gets the string that was passed to the server
                        String command = inputLine.split("@")[0]; //command words that is separated by @ symbol
                        String[] info = inputLine.split("@")[1].split("&"); ; // Separated data

                        switch (command) {
                            case "d_login":   // Command to login as a doctor
                                System.out.println("Doctor login");
                                String usernameDoctor = info[0];
                                String passwordDoctor = info[1];
                                System.out.println(usernameDoctor + " " + passwordDoctor);

                                activeUser = login(usernameDoctor, passwordDoctor);
                                if (activeUser == null) writer.println("false");
                                writer.println("true");
                                break;
                            case "p_login":  // Command to login as a patient
                                System.out.println("Patient login");
                                String usernamePatient = info[0];
                                String passwordPatient = info[1];
                                System.out.println(usernamePatient + " " + passwordPatient);

                                activeUser = login(usernamePatient, passwordPatient);
                                if (activeUser == null) writer.println("false");
//                                updatePatientInfo((Patient) activeUser, "Write Notes", "testing notes");
                                writer.println("true");

                                break;
                            case "adm_login": // Command to login as an admin
                                System.out.println("Admin login");
                                String usernameAdmin = info[0];
                                String passwordAdmin = info[1];

                                activeUser = login(usernameAdmin, passwordAdmin);
                                System.out.println(activeUser);
                                if (activeUser == null) writer.println("false");
                                writer.println("true");

                                break;
                            case "p_reg":  // Command to register new patient
                                System.out.println("Patient Registration");
                                String usernameNewPatient = info[0];
                                String passwordNewPatient = info[1];
                                String occupationNewPatient = info[2];
                                String genderNewPatient = info[3];
                                int ageNewPatient = Integer.parseInt(info[4]);
                                String reasonForVisitNewPatient = info[5];

                                activeUser = addPatient(usernameNewPatient, passwordNewPatient, occupationNewPatient, genderNewPatient, ageNewPatient, reasonForVisitNewPatient);
//                                if(activeUser == null) writer.println("false");
                                writer.println("true");
                                break;
                            case "d_reg":  // Command to register new doctor
                                System.out.println("Doctor Registration");
                                String usernameNewDoctor = info[0];
                                String passwordNewDoctor = info[1];
                                String occupationNewDoctor = info[2];
                                String genderNewDoctor = info[3];
                                int ageNewDoctor = Integer.parseInt(info[4]);
                                String specializationNewDoctor = info[5];
                                activeUser = addDoctor(usernameNewDoctor,passwordNewDoctor,occupationNewDoctor,genderNewDoctor,ageNewDoctor,specializationNewDoctor);
                                writer.println("true");
                                break;
                            case "_logout": // Logout command
                                if(activeUser != null) activeUser = null;
                                writer.println("logoutComplete");
                                break;
                            case "p_find": // Command to find and choose to work with the patient from the list
                                String patientUsernameToFind = info[0];
                                int patientAgeToFind = Integer.parseInt(info[1]);
                                User tempPatient = searchPatient(patientUsernameToFind, patientAgeToFind);
                                patientDoctorWorksWith = (Patient) tempPatient;
                                if(tempPatient != null){
                                    String responseToClient = patientDoctorWorksWith.getName()+"&"+patientDoctorWorksWith.getAge()+"&"+patientDoctorWorksWith.getReasonForVisit()+"&"+patientDoctorWorksWith.getMeds();
                                    writer.println(responseToClient);
                                }else{
                                    writer.println("false");
                                }
//                                System.out.println(tempPatient.toString());
                                break;
                            case "_isPatientChosen": // Command that allows to check if doctor chose the patient before making any changes
                                boolean rez = patientDoctorWorksWith != null;
                                writer.println(rez);
                                break;
                            case "p_infoUpdate": // Command to update patient info from doctor scene
                                String doctorAction = info[0];
                                String infoToUpdate = info[1];
                                Patient temporaryPatientToUpdate = updatePatientInfo(patientDoctorWorksWith, doctorAction, infoToUpdate);
//                                System.out.println(temporaryPatientToUpdate);
                                if(temporaryPatientToUpdate != null){
                                    patientDoctorWorksWith = temporaryPatientToUpdate;
                                    writer.println("true");
                                }else{
                                    writer.println("false");
                                }
                                break;
                            case "p_notesReq": // Command to request the patient doctor notes
                                String currentPatientNotes = ((Patient) activeUser).viewNotes().trim();
                                if( currentPatientNotes.equals("")){
                                    writer.println("The doctor has not left you any notes yet");

                                }else{
                                    System.out.println("go to client");
                                    writer.println(currentPatientNotes);
                                }
                                break;
                            case "p_medsReq": // Command to request the patient medications
                                String currentPatientMeds = "";
                                for(String med: ((Patient) activeUser).getMeds()){
                                    currentPatientMeds = currentPatientMeds + med +"&";
                                }
                                if(currentPatientMeds.equals("")){
                                    writer.println("The doctor has not left you any medications");
                                }else{
                                    writer.println(currentPatientMeds);
                                }
                                System.out.println(currentPatientMeds);
                                break;
                            case "p_makeApt": // Command to update patient appointment
                                String setNewApt = info[0];
                                Boolean rezNewApt = updatePatientInfo((Patient) activeUser, "p_makeApt", setNewApt) != null;
                                writer.println(rezNewApt);
                                break;
                            case "p_payment": // Command to update the patient payment info
                                String paymentInfo = info[0];
                                Boolean rezPayment = updatePatientInfo((Patient) activeUser, "p_payment", paymentInfo) != null;
                                writer.println(rezPayment);
                                break;
                            case "_activeUserReq":
                                if(activeUser != null){
                                    writer.println(activeUser.getName());
                                }else{
                                    writer.println("false");
                                }
                                break;
                            default:
                                writer.println("false");


                        }

                        if ("exit".equalsIgnoreCase(inputLine)) {
                            System.out.println("Client Exits");
                            break;
                        }
                        System.out.println("<<<>>>");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Patient updatePatientInfo(Patient patientToUpdate, String updateCode, String infoToUpdate){
        for(User user: userList){
            if(user instanceof Patient && ((Patient) user).getName().equals(patientToUpdate.getName()) && ((Patient) user).getAge() == patientToUpdate.getAge()){
                if(updateCode.equalsIgnoreCase("Prescribe Meds")){
                    ((Patient) user).addMed(infoToUpdate);
                    return (Patient) user;
                }else if(updateCode.equalsIgnoreCase("Write Notes")){
                    ((Patient) user).addNotes(infoToUpdate);
                    return (Patient) user;
                } else if (updateCode.equalsIgnoreCase("p_makeApt")) {
                    ((Patient) user).makeApt(infoToUpdate);
                    return (Patient) user;
                } else if (updateCode.equalsIgnoreCase("p_payment")) {
                    ((Patient) user).setPaymentInfo(infoToUpdate);
                    return (Patient) user;
                }
            }
        }

        return null;
    }

    public static User searchPatient(String username, int age){
        for(User user: userList){
            if(user.getName().equalsIgnoreCase(username) && user.getAge()== age ){
                return user;
            }
        }
        return null;
    }


    public static User addDoctor(String usernameNewDoctor,
                                 String passwordNewDoctor,
                                 String occupationNewDoctor,
                                 String genderNewDoctor,
                                 int ageNewDoctor,
                                 String specializationNewDoctor
    ) {
        Doctor newDoctor = new Doctor(usernameNewDoctor,occupationNewDoctor,genderNewDoctor,ageNewDoctor,specializationNewDoctor,passwordNewDoctor);
        userList.add(newDoctor);
        return newDoctor;
    }
    public static User addPatient(String usernameNewPatient,
                                  String passwordNewPatient,
                                  String occupationNewPatient,
                                  String genderNewPatient,
                                  int ageNewPatient,
                                  String reasonForVisitNewPatient
    ) {
        Patient newPatient = new Patient(usernameNewPatient, occupationNewPatient, genderNewPatient, ageNewPatient, reasonForVisitNewPatient, passwordNewPatient);
        userList.add(newPatient);
        return newPatient;
    }

    public static User login(String username, String password) {
        for (User user : userList) {
            if (user.getName().equals(username)) {
                if (user.login(username, password)) {
                    return user;
                }
            }
        }
        return null;
    }
}
