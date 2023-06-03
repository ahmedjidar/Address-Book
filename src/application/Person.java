package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// bean with property interface feature
public class Person {
    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Integer age;
    private final String apogee;

    // object constructor
    public Person(Integer id, String firstName, String lastName, String email, Integer age, String apogee) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.apogee = apogee;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge(){
        return age;
    }

    public String getApogee(){
        return apogee;
    }

    // implementing a bean property interface to wrap the values and let the list track their changes
    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public SimpleStringProperty firstNameProperty() {
        return new SimpleStringProperty(firstName);
    }

    public SimpleStringProperty lastNameProperty() {
        return new SimpleStringProperty(lastName);
    }

    public SimpleStringProperty emailProperty() {
        return new SimpleStringProperty(email);
    }

    public SimpleIntegerProperty ageProperty(){
        return new SimpleIntegerProperty(age);
    }

    public SimpleStringProperty apogeeProperty(){
        return new SimpleStringProperty(apogee);
    }
}