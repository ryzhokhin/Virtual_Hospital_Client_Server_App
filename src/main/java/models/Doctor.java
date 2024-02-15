package models;

import java.util.ArrayList;

public class Doctor extends User{

    private String specialization;

    public Doctor(String name, String occupation, String gender, int age, String specialization, String password) {
        super(name, occupation, gender,age,password);
        this.specialization = specialization;
    }

    /*

     */
//    public String prescribeMeds(String patientName, String med) {
//        // Implementation for prescribing medications
//        Patient patient=(Patient) Server.getInstance().getInfo(patientName,"patient");
//        if(patient==null){
//            return "Patient doesn't exist";
//        }
//        patient.addMed(med);
//        return "Success";
//    }
//
//    public String removeMeds(String patientName, String med) {
//        // Implementation for prescribing medications
//        Patient patient=(Patient) Server.getInstance().getInfo(patientName,"patient");
//        if(patient==null){
//            return "Patient doesn't exist";
//        }
//        patient.removeMed(med);
//        return "Success";
//    }
//
//
//
//    public String viewPatientNotes(String patientName) {
//        Patient patient=(Patient)Server.getInstance().getInfo(patientName,"patient");
//        return patient.viewNotes();
//    }
//
//    public ArrayList<Patient> getPatients() {
//        return Server.getInstance().getPatientList(this);
//    }
//    public void setSpecialization(String newSpecialization){
//        specialization=newSpecialization;
//    }
//    /*
//   This method adds another patient to the userList on the server class
//   @param the information of the new patient that is going to be added
//   @return the String to say if it is successful or an error occured
//    */
//    public String addPatient(String name, String occupation, String gender, int age, String reasonForVisit,String password){
//        return Server.getInstance().registerPatient(name, occupation, gender, age, reasonForVisit,password,this);
//
//    }
}