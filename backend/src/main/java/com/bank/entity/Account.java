package com.bank.entity;

import com.bank.security.CryptoConverter;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "balance")
    private String balance;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }
}
