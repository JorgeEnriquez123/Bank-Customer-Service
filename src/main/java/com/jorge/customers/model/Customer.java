package com.jorge.customers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private String id;
    private CustomerType customerType; // Tipo de cliente (PERSONAL o BUSINESS)
    private String email;
    private String phoneNumber;
    private String address;
    // --- Campos Específicos para Cliente PERSONAL ---
    private String dni;
    private String firstName;
    private String lastName;

    // --- Campos Específicos para Cliente EMPRESARIAL ---
    private String ruc;
    private String businessName;
}