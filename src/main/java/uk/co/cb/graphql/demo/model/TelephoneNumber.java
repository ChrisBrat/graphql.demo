package uk.co.cb.graphql.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TelephoneNumber {
    private long id;
    private String number;
}
