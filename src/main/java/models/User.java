package models;

public abstract class User {
    private String name;
    private String occupation;
    private String gender;
    private int age;
    private String password;

    public User(String name, String occupation, String gender, int age, String password) {
        this.name = name;
        this.occupation = occupation;
        this.gender = gender;
        this.age = age;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getOccupation() {
        return this.occupation;
    }

    public String getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public boolean login(String username, String password) {
        return this.password.equals(password) && this.name.equals(username);
    }
}

