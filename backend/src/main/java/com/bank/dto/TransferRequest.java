package com.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "Destination account is required")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Invalid account number format")
    private String toAccount;

    @Positive(message = "Transfer amount must be strictly positive")
    private BigDecimal amount;

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
