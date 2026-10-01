package utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum StatesCities {
    NCR("NCR", City.DELHI, City.GURGAON, City.NOIDA),
    UTTAR_PRADESH("Uttar Pradesh", City.AGRA, City.LUCKNOW, City.MERUT),
    HARYANA("Haryana", City.KARNAL, City.PANIPAT),
    RAJASTHAN("Rajasthan", City.JAIPUR, City.JODHPUR);

    private final String name;
    private final List<City> cities;

    StatesCities(String name, City... cities) {
        this.name = name;
        this.cities = Arrays.asList(cities);
    }

    @Getter
    public enum City {
        DELHI("Delhi"),
        GURGAON("Gurgaon"),
        NOIDA("Noida"),
        AGRA("Agra"),
        LUCKNOW("Lucknow"),
        MERUT("Merut"),
        KARNAL("Karnal"),
        PANIPAT("Panipat"),
        JAIPUR("Jaipur"),
        JODHPUR("Jodhpur");

        private final String name;

        City(String name) {
            this.name = name;
        }

    }
}