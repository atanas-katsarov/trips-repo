package uni.master.trips.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Category {

    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id == category.id && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NonNull
    @Override
    //Returns the id and the email of the user
    public String toString() {
        return "Category { " +
                "ID : " + id + " Name : " + name;
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
}
