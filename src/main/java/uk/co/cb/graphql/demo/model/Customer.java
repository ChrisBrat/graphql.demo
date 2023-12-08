package uk.co.cb.graphql.demo.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Customer {
    @EqualsAndHashCode.Include
    private Long id;
    private String firstName;
    private String lastName;
    private List<TelephoneNumber> telephoneNumbers;
}
