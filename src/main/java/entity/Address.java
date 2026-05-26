package entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "apartment_num")
    private String apartmentNum;
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    public Address() {}

    public Address(String country, String region, String city, String street, String streetNum, String apartmentNum, String postalCode) {
        setCountry(country);
        setCity(city);
        setStreet(street);
        this.apartmentNum = apartmentNum;
        setPostalCode(postalCode);
    }

    public String getCountry() { return country; }
    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getApartmentNum() { return apartmentNum; }
    public String getPostalCode() { return postalCode; }

    public void setCountry(String country) {
        Objects.requireNonNull(country, "Country cannot be null");
        this.country = country;
    }

    public void setCity(String city) {
        Objects.requireNonNull(city, "City cannot be null");
        this.city = city;
    }

    public void setStreet(String street) {
        Objects.requireNonNull(street, "Street cannot be null");
        this.street = street;
    }

    public void setApartmentNum(String apartmentNum) {
        this.apartmentNum = apartmentNum;
    }

    public void setPostalCode(String postalCode) {
        Objects.requireNonNull(postalCode, "Postal code cannot be null");
        this.postalCode = postalCode;
    }
}