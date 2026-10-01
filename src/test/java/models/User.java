package models;

import com.github.javafaker.Faker;
import utils.Hobbies;
import utils.StatesCities;


public record User(String firstName, String lastName, String email, String phoneNumbe, String subject,
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

        return new User(fvk.name().firstName(), fvk.name().lastName(), fvk.internet().emailAddress(), fvk.phoneNumber().toString(), fvk.company().catchPhrase(), StatesCities.HARYANA,
                fvk.address().country(), StatesCities.HARYANA.getCities().get(1), fvk.address().streetName(), fvk.address().buildingNumber(), fvk.address().zipCode(),
                fvk.date().birthday().toString(), fvk.demographic().sex(), Hobbies.MUSIC);

    }
}

