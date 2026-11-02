package models;

import com.github.javafaker.Faker;
import utils.Hobbies;
import utils.StatesCities;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public record User(String firstName, String lastName, String email, String phoneNumber, List<String> subject,
                   StatesCities state,
                   String country, StatesCities.City city, String street, String house, String postCode, String dob,
                   String sex, Hobbies hobby) {

    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(country).append(", ");
        sb.append(postCode).append(", ");
        sb.append(state).append(", ");
        sb.append(city).append(", ");
        sb.append(street).append(" ").append(house);
        return sb.toString();
    }

    public static User getGeneratedUser() {
        Faker fvk = new Faker();
        String firstName = fvk.name().firstName();
        String lastName = fvk.name().lastName();
        String email = firstName + "_" + lastName + System.currentTimeMillis() + "@" + fvk.internet().domainWord() + ".com";
        List<String> subjects = List.of("Maths",
                "Physics",
                "Chemistry",
                "English",
                "Hindi",
                "Biology",
                "Computer Science",
                "Commerce",
                "Accounting",
                "Economics",
                "Arts",
                "Social Studies",
                "History",
                "Civics");

        return new User(firstName, lastName, email, fvk.numerify("##########"), subjects, StatesCities.HARYANA,
                fvk.address().country(), StatesCities.HARYANA.getCities().get(1), fvk.address().streetName(), fvk.address().buildingNumber(), fvk.address().zipCode(),
                new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(fvk.date().birthday()), fvk.demographic().sex(), Hobbies.MUSIC);

    }

    public static User withFirstName(String firstName) {
        User exampleEntity = getGeneratedUser();
        return new User(firstName, exampleEntity.lastName, exampleEntity.email, exampleEntity.phoneNumber, exampleEntity.subject, exampleEntity.state,
                exampleEntity.country, exampleEntity.city, exampleEntity.street, exampleEntity.house, exampleEntity.postCode, exampleEntity.dob, exampleEntity.sex, exampleEntity.hobby);
    }

    public static User withLastName(String lastName) {
        User exampleEntity = getGeneratedUser();
        return new User(exampleEntity.firstName, lastName, exampleEntity.email, exampleEntity.phoneNumber, exampleEntity.subject, exampleEntity.state,
                exampleEntity.country, exampleEntity.city, exampleEntity.street, exampleEntity.house, exampleEntity.postCode, exampleEntity.dob, exampleEntity.sex, exampleEntity.hobby);
    }

    public static User withPhoneNumber(String phoneNumber) {
        User exampleEntity = getGeneratedUser();
        return new User(exampleEntity.firstName, exampleEntity.lastName, exampleEntity.email, phoneNumber, exampleEntity.subject, exampleEntity.state,
                exampleEntity.country, exampleEntity.city, exampleEntity.street, exampleEntity.house, exampleEntity.postCode, exampleEntity.dob, exampleEntity.sex, exampleEntity.hobby);
    }

    public static User withGender(String gender) {
        User exampleEntity = getGeneratedUser();
        return new User(exampleEntity.firstName, exampleEntity.lastName, exampleEntity.email, exampleEntity.phoneNumber, exampleEntity.subject, exampleEntity.state,
                exampleEntity.country, exampleEntity.city, exampleEntity.street, exampleEntity.house, exampleEntity.postCode, exampleEntity.dob, gender, exampleEntity.hobby);
    }
}

