package hotel.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final String email;
    private final LocalDate birthDate;
    private transient String password;

    public Guest(int id, String name, String email, String password, LocalDate birthDate) {
        if (id < 0) {
            throw new IllegalArgumentException("Guest id must not be negative");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Guest name must not be blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Guest email must not be blank");
        }
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Guest birth date must not be in the future");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Integer getAge() {
        return getAgeOn(LocalDate.now());
    }

    public Integer getAgeOn(LocalDate date) {
        if (birthDate == null || date == null || birthDate.isAfter(date)) {
            return null;
        }
        return Period.between(birthDate, date).getYears();
    }

    @Override
    public String toString() {
        return "Guest [id=" + id + ", name=" + name + ", email=" + email + ", birthDate=" + birthDate + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Guest other)) {
            return false;
        }
        return id == other.id;
    }
}
