package com.bank.entity;

import com.bank.security.CryptoConverter;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "email")
    private String email;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "full_name")
    private String fullName;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
