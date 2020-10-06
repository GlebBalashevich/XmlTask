package by.balashevich.periodicals.entity;

import java.util.Date;

public class Publisher {
    public enum Country{
        USA,
        RUSSIA,
        ENGLAND,
        BELARUS
    }

    private Country country;
    private Date licenseExpiration;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getLicenseExpiration() {
        return licenseExpiration;
    }

    public void setLicenseExpiration(Date licenseExpiration) {
        this.licenseExpiration = licenseExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Publisher publisher = (Publisher) o;

        return country == publisher.country
                && licenseExpiration.equals(publisher.licenseExpiration);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * result + country.ordinal();
        result += 37 * result + licenseExpiration.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return String.format("Publisher country: %s, licenseExpiration: %tD", country.name(), licenseExpiration);
    }
}
