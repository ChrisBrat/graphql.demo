package uk.co.cb.graphql.demo.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Builder
@Getter
@Setter
public class TelephoneNumber {
    @EqualsAndHashCode.Include
    private long id;
    private String number;
}
