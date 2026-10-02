package models;

import com.github.javafaker.Faker;
import utils.Hobbies;
import utils.StatesCities;

import java.util.List;


public record User(String firstName, String lastName, String email, String phoneNumbe, List<String> subject,
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
                fvk.date().birthday().toString(), fvk.demographic().sex(), Hobbies.MUSIC);

    }
}

