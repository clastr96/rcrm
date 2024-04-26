package com.example.raynetcrm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNumber;

    private String title;

    private String email;

    private String phone;

    private Instant upsertDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(regNumber, client.regNumber) &&
                Objects.equals(title, client.title) &&
                Objects.equals(email, client.email) &&
                Objects.equals(phone, client.phone) &&
                Objects.equals(upsertDate, client.upsertDate);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id.intValue();
        hash = 31 * hash + (regNumber == null ? 0 : regNumber.hashCode());
        hash = 31 * hash + (title == null ? 0 : title.hashCode());
        hash = 31 * hash + (email == null ? 0 : email.hashCode());
        hash = 31 * hash + (phone == null ? 0 : phone.hashCode());
        hash = 31 * hash + (upsertDate == null ? 0 : upsertDate.hashCode());
        return hash;
    }
}
