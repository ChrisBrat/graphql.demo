package uk.co.cb.graphql.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import uk.co.cb.graphql.demo.entity.CustomerEntity;
import uk.co.cb.graphql.demo.entity.QCustomerEntity;
import uk.co.cb.graphql.demo.model.Customer;
import uk.co.cb.graphql.demo.model.CreateCustomer;
import uk.co.cb.graphql.demo.model.CreateTelephoneNumber;
import uk.co.cb.graphql.demo.model.TelephoneNumber;
import uk.co.cb.graphql.demo.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    public static Map<Long, List<TelephoneNumber>> globalTelephoneNumbers = new HashMap<>();

    @QueryMapping
    public Customer customer(@Argument Long id){

        Optional<Customer> result = customerRepository.
                findOne(QCustomerEntity.customerEntity.id.eq(id)).
                map(x ->
                     Customer.
                            builder().
                            id(x.getId()).
                            firstName(x.getFirstName()).
                            lastName(x.getLastName()).
                            build()
                );
        return result.get();
    }

    @QueryMapping
    public Collection<Customer> customers(){
        List<Customer> result = customerRepository.
                findAll().
                stream().
                map(x ->
                        Customer.
                                builder().
                                id(x.getId()).
                                firstName(x.getFirstName()).
                                lastName(x.getLastName()).
                                build()
                ).collect(Collectors.toList());
        return result;
    }

    @BatchMapping
    public Map<Customer, List<TelephoneNumber>> telephoneNumbers(List<Customer> customers){
        Map<Customer, List<TelephoneNumber>> results = new HashMap<>();

        for (Customer customer : customers){
            List<TelephoneNumber> telephoneNumbers = globalTelephoneNumbers.get(customer.getId());
            results.put(customer,telephoneNumbers);
        }
        return results;
    }

    @MutationMapping
    public Customer createCustomer(@Argument CreateCustomer createCustomer){

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName(createCustomer.getFirstName());
        customerEntity.setLastName(createCustomer.getLastName());
        customerEntity = customerRepository.save(customerEntity);

        Optional<Customer> result = customerRepository.
                findOne(QCustomerEntity.customerEntity.id.eq(customerEntity.getId())).
                map(x ->
                        Customer.
                                builder().
                                id(x.getId()).
                                firstName(x.getFirstName()).
                                lastName(x.getLastName()).
                                build()
                );

        //addNewCustomerTelephoneNumbers(inputCustomer, customerId);
        return result.get();
    }

    private void addNewCustomerTelephoneNumbers(CreateCustomer inputCustomer,
                                                long customerId) {
        /*List<CreateTelephoneNumber> inputTelephoneNumbers = inputCustomer.getTelephoneNumbers();
        if (inputTelephoneNumbers == null) {
            return;
        }
        List<TelephoneNumber> telephoneNumbers = globalTelephoneNumbers.
                getOrDefault(customerId,new ArrayList<>());
        globalTelephoneNumbers.put(customerId, telephoneNumbers);

        for (int i = 0 ; i < inputTelephoneNumbers.size() ; i++) {
            TelephoneNumber telephoneNumber = TelephoneNumber.builder().
                    id(telephoneNumberId++).
                    number(inputTelephoneNumbers.get(i).
                            getNumber()).
                    build();
            telephoneNumbers.add(telephoneNumber);
        }*/
    }
}
