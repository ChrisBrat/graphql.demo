package uk.co.cb.graphql.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateCustomer {
    private String firstName;
    private String lastName;
    private List<CreateTelephoneNumber> telephoneNumbers;
}
