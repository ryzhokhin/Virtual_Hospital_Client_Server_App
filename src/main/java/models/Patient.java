package models;

import java.util.ArrayList;

public class Patient extends User {
    private String reasonForVisit;
    private String apt;
    private ArrayList<String> meds;
    private String  paymentInfo;
    private String notes;
//    private Doctor doctor;

    /*
    This constructor creates a new patient with the doctor that created it as the doctor of the patient
    @param is the information of the patient and the doctor that created the patient
     */
    public Patient(String name, String occupation, String gender, int age, String reasonForVisit,String password/*,Doctor doctor*/) {
        super(name,occupation,gender,age,password);
        this.reasonForVisit = reasonForVisit;
        this.apt="";
        this.meds=new ArrayList<String>();
        this.notes="";
//        this.doctor=doctor;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }


    public String viewNotes() {
        if(notes.equals("") ){
            return "patient does not have any notes";
        }
        return notes;
    }
    public void addNotes(String note){
        this.notes+="\n"+note;
    }
    public void makeApt(String newApt) {
        apt = newApt;
    }

    public String getApt() {
        return apt;
    }

    public void addMed(String medication){
        meds.add(medication);
    }
    public void removeMed(String medication){
        meds.remove(medication);
    }



    public ArrayList<String> getMeds() {
        return meds;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }


}
