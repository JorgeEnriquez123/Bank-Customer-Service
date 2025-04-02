package com.jorge.customers.service.impl;

import com.jorge.customers.mapper.CustomerMapper;
import com.jorge.customers.model.Customer;
import com.jorge.customers.model.CustomerRequest;
import com.jorge.customers.model.CustomerResponse;
import com.jorge.customers.model.CustomerType;
import com.jorge.customers.repository.CustomerRepository;
import com.jorge.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> getCustomerByDni(String dni) {
        return customerRepository.findByDni(dni)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente con dni: " + dni + " no encontrado")))
                .map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        return customerRepository.save(customerMapper.mapToCustomer(customerRequest))
                .map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> updateCustomerByDni(String dni, CustomerRequest customerRequest) {
        return customerRepository.findByDni(dni)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente con dni: " + dni + " no encontrado")))
                .flatMap(existingCustomer ->
                        customerRepository.save(updateCustomerFromRequest(existingCustomer, customerRequest)))
                .map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<Void> deleteCustomerByDni(String dni) {
        return customerRepository.deleteByDni((dni));
    }

    public Customer updateCustomerFromRequest(Customer existingCustomer, CustomerRequest customerRequest) {
        existingCustomer.setCustomerType(CustomerType.valueOf(customerRequest.getCustomerType().name()));
        existingCustomer.setEmail(customerRequest.getEmail());
        existingCustomer.setPhoneNumber(customerRequest.getPhoneNumber());
        existingCustomer.setAddress(customerRequest.getAddress());
        existingCustomer.setDni(customerRequest.getDni());
        existingCustomer.setFirstName(customerRequest.getFirstName());
        existingCustomer.setLastName(customerRequest.getLastName());
        return existingCustomer;
    }
}
