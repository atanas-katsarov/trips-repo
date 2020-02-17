package uni.master.trips.entities;

import java.io.Serializable;
import java.util.Objects;

public class Site implements Serializable {
    private String name;
    private String description;

    public Site (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Site () {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Objects.equals(name, site.name) &&
                Objects.equals(description, site.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
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
}
