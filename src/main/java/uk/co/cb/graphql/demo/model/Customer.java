package uk.co.cb.graphql.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Customer {
    private Long id;
    private String firstname;
    private String lastName;
    private List<TelephoneNumber> telephoneNumbers;
}
