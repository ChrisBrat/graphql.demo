package uk.co.cb.graphql.demo.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import uk.co.cb.graphql.demo.model.Customer;
import uk.co.cb.graphql.demo.model.InputCustomer;
import uk.co.cb.graphql.demo.model.InputTelephoneNumber;
import uk.co.cb.graphql.demo.model.TelephoneNumber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Controller
public class CustomerController {

    private long telephoneNumberId = 0;
    private Map<Long, List<TelephoneNumber>> globalTelephoneNumbers = new HashMap<>();
    private Map<Long, Customer> customers = new HashMap<>();

    @QueryMapping
    public Customer customer(@Argument Long id){
        return customers.get(id);
    }

    @QueryMapping
    public Collection<Customer> customers(){
        return customers.values();
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
    public Customer createCustomer(@Argument InputCustomer inputCustomer){
        long customerId = customers.size();

        Customer customer = Customer.builder().
                id(customerId).
                firstName(inputCustomer.getFirstName()).
                lastName(inputCustomer.getLastName()).
                build();

        customers.put(customerId, customer);
        addNewCustomerTelephoneNumbers(inputCustomer, customerId);
        return customer;
    }

    private void addNewCustomerTelephoneNumbers(InputCustomer inputCustomer,
                                                long customerId) {
        List<InputTelephoneNumber> inputTelephoneNumbers = inputCustomer.getInputTelephoneNumbers();
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
        }
    }
}
