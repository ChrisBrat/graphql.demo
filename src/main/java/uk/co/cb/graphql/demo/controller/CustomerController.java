package uk.co.cb.graphql.demo.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import uk.co.cb.graphql.demo.model.Customer;
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

    Map<Long, List<TelephoneNumber>> globalTelephoneNumbers = new HashMap<>();
    Map<Long, Customer> customers = new HashMap<>();
    {

        int j = 1;
        for (long i = 0; i < 10; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customer.setFirstname("fn="+UUID.randomUUID().toString());
            customer.setLastName("ln="+UUID.randomUUID().toString());
            int end = j + new Random().nextInt(3);
            for (int k = j; k < end ; k++){
                j++;
                TelephoneNumber telephoneNumber = new TelephoneNumber();
                telephoneNumber.setId(k);
                telephoneNumber.setNumber("N"+k+"-"+j);

                List<TelephoneNumber> telephoneNumberList = globalTelephoneNumbers.
                        getOrDefault(customer.getId(),new ArrayList<>());
                telephoneNumberList.add(telephoneNumber);
                globalTelephoneNumbers.put(customer.getId(),telephoneNumberList);
            }

            customers.put(i, customer);
        }
    }

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

}
