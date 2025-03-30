package com.jorge.customers.service.impl;

import com.jorge.customers.mapper.CustomerMapper;
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
    public Mono<CustomerResponse> getCustomerById(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente con id: " + id + " no encontrado")))
                .map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        if(customerRequest.getCustomerType().equals(CustomerType.BUSINESS.toString())){
            if(customerRequest.getRuc() == null || customerRequest.getRuc().trim().isEmpty()){
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC es obligatorio para clientes empresariales."));
            }
        }
        if (customerRequest.getCustomerType().equals(CustomerType.PERSONAL.toString())){
            if (customerRequest.getRuc() != null && !customerRequest.getRuc().trim().isEmpty()) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUC no debe ser proporcionado para clientes personales"));
            }
        }
        return customerRepository.save(customerMapper.mapToCustomer(customerRequest))
                .map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> updateCustomerById(String id, CustomerRequest customerRequest) {
        return customerRepository.findById(id)
                .flatMap(existingCustomer -> {
                    existingCustomer.setCustomerType(CustomerType.valueOf(customerRequest.getCustomerType()));
                    if (customerRequest.getCustomerType().equals("BUSINESS")) {
                        existingCustomer.setRuc(customerRequest.getRuc());
                        existingCustomer.setBusinessName(customerRequest.getBusinessName());
                    }
                    existingCustomer.setEmail(customerRequest.getEmail());
                    existingCustomer.setPhoneNumber(customerRequest.getPhoneNumber());
                    existingCustomer.setAddress(customerRequest.getAddress());
                    existingCustomer.setDni(customerRequest.getDni());
                    existingCustomer.setFirstName(customerRequest.getFirstName());
                    existingCustomer.setLastName(customerRequest.getLastName());
                    return customerRepository.save(existingCustomer);
                }).map(customerMapper::mapToCustomerResponse);
    }

    @Override
    public Mono<Void> deleteCustomerById(String id) {
        return customerRepository.deleteById(id);
    }
}
