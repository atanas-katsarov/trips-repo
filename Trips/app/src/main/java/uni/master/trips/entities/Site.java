package uni.master.trips.entities;

import java.io.Serializable;
import java.util.Objects;

public class Site implements Serializable {

    private int id;
    private String name;
    private String description;
    private String countryName;
    private int userId;
    private int categoryId;

    public Site() {
    }

    public Site(String name, String description, String countryName, int userId, int categoryId) {
        this.id = 31 * Objects.hash(name) + Objects.hash(description, countryName);
        this.name = name;
        this.description = description;
        this.countryName = countryName;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public Site(int id, String name, String description, String countryName, int userId, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.countryName = countryName;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return id == site.id &&
                name.equals(site.name) &&
                description.equals(site.description) &&
                countryName.equals(site.countryName);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
