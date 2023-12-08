package uk.co.cb.graphql.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateTelephoneNumber {
    private long id;
    private long customerId;
    private String number;
}
