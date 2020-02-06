package uni.master.trips.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class User {

    private int id;
    private String email;

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String email) {
        id = 31 * Objects.hash(email);
        this.email = email;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NonNull
    @Override
    //Returns the id and the email of the user
    public String toString() {
        return "User { " +
                "ID : " + id +
                " Email : " + email;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
