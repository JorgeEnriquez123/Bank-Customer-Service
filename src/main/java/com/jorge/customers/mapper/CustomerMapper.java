package com.jorge.customers.mapper;

import com.jorge.customers.model.Customer;
import com.jorge.customers.model.CustomerRequest;
import com.jorge.customers.model.CustomerResponse;
import com.jorge.customers.model.CustomerType;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer mapToCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .customerType(CustomerType.valueOf(customerRequest.getCustomerType()))
                .email(customerRequest.getEmail())
                .phoneNumber(customerRequest.getPhoneNumber())
                .address(customerRequest.getAddress())
                .dni(customerRequest.getDni())
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .ruc(customerRequest.getRuc())
                .businessName(customerRequest.getBusinessName())
                .build();
    }

    public CustomerResponse mapToCustomerResponse(Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setCustomerType(customer.getCustomerType().name());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setPhoneNumber(customer.getPhoneNumber());
        customerResponse.setAddress(customer.getAddress());
        customerResponse.setDni(customer.getDni());
        customerResponse.setFirstName(customer.getFirstName());
        customerResponse.setLastName(customer.getLastName());
        customerResponse.setRuc(customer.getRuc());
        customerResponse.setBusinessName(customer.getBusinessName());
        return customerResponse;
    }
}
