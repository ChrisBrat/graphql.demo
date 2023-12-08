package uk.co.cb.graphql.demo.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import uk.co.cb.graphql.demo.entity.CustomerEntity;
import uk.co.cb.graphql.demo.entity.QCustomerEntity;
import uk.co.cb.graphql.demo.entity.QTelephoneNumberEntity;
import uk.co.cb.graphql.demo.entity.TelephoneNumberEntity;
import uk.co.cb.graphql.demo.model.CreateCustomer;
import uk.co.cb.graphql.demo.model.CreateTelephoneNumber;
import uk.co.cb.graphql.demo.model.Customer;
import uk.co.cb.graphql.demo.model.TelephoneNumber;
import uk.co.cb.graphql.demo.repository.CustomerRepository;
import uk.co.cb.graphql.demo.repository.TelephoneNumberRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TelephoneNumberRepository telephoneNumberRepository;

    @Autowired
    private EntityManager entityManager;

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
        List<Customer> result = StreamSupport.stream(customerRepository.
                findAll().spliterator(),false).
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

        Map<Long, Customer> customerMap = customers.stream().
                collect(Collectors.toMap(x-> x.getId(), Function.identity()));

        Set<Long> customerIds = customerMap.keySet();

        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QTelephoneNumberEntity qTelephoneNumberEntity = QTelephoneNumberEntity.telephoneNumberEntity;
        List<TelephoneNumberEntity> telephoneNumberEntitys =
                factory.query().
                        select(qTelephoneNumberEntity).
                        from(qTelephoneNumberEntity).
                        where(qTelephoneNumberEntity.customer.id.
                                in(customerIds)).fetch();

        Map<Customer, List<TelephoneNumber>> result = new HashMap<>();
        for (TelephoneNumberEntity telephoneNumberEntity: telephoneNumberEntitys){
            Customer customer = customerMap.get(telephoneNumberEntity.getCustomer().getId());

            List<TelephoneNumber> telephoneNumbers = result.getOrDefault(customer, new ArrayList<>());
            telephoneNumbers.add(TelephoneNumber.builder().
                            id(telephoneNumberEntity.getId()).
                            number(telephoneNumberEntity.getNumber()).
                            build());
            result.put(customer, telephoneNumbers);
        }

        return result;
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

        addNewCustomerTelephoneNumbers(createCustomer, customerEntity);
        return result.get();
    }

    private void addNewCustomerTelephoneNumbers(CreateCustomer inputCustomer,
                                                CustomerEntity customerEntity) {
        List<CreateTelephoneNumber> inputTelephoneNumbers = inputCustomer.getTelephoneNumbers();
        if (inputTelephoneNumbers == null) {
            return;
        }

        List<TelephoneNumberEntity> savable = inputTelephoneNumbers.stream().map(x -> {
                TelephoneNumberEntity telephoneNumberEntity = new TelephoneNumberEntity();
                telephoneNumberEntity.setNumber(x.getNumber());
                telephoneNumberEntity.setCustomer(customerEntity);
                return telephoneNumberEntity;
        }).collect(Collectors.toList());

        telephoneNumberRepository.saveAll(savable);
    }
}
