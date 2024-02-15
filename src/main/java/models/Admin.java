package models;

public class Admin extends User {
    public Admin(String name, String occupation, String gender, int age, String password) {
        super(name, occupation, gender, age, password);
    }

//    public String deleteUser(String name, String occupation, String gender, int age) {
//        return Server.getInstance().removeUser(name, occupation, gender, age);
//    }
//
//    public String addDoctor(String name, String occupation, String gender, int age, String specialization, String password) {
//        return Server.getInstance().registerDoctor(name, occupation, gender, age, specialization, password);
//    }
//
//    public String changeDoctor(String doctorName, String newSpecialization) {
//        Doctor user = (Doctor)Server.getInstance().getInfo(doctorName, "doctor");
//        if (user == null) {
//            return "Doctor do not exist";
//        } else {
//            user.setSpecialization(newSpecialization);
//            return "Success";
//        }
//    }
//
//    public String addAdmin(String name, String occupation, String gender, int age, String password) {
//        return Server.getInstance().registerAdmin(name, occupation, gender, age, password);
//    }
}
