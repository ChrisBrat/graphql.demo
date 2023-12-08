package uk.co.cb.graphql.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InputCustomer {
    private String firstName;
    private String lastName;
    private List<InputTelephoneNumber> inputTelephoneNumbers;
}
