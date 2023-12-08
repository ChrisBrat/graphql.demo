package uk.co.cb.graphql.demo.controller;

import lombok.SneakyThrows;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import uk.co.cb.graphql.demo.model.TelephoneNumber;
import uk.co.cb.graphql.demo.model.UpdateTelephoneNumber;

import java.util.List;
import java.util.Optional;

@Controller
public class TelephoneController {

    @SneakyThrows
    @MutationMapping
    public TelephoneNumber updateTelephoneNumber(@Argument UpdateTelephoneNumber updateTelephoneNumber){
        List<TelephoneNumber> telephoneNumbers = CustomerController.globalTelephoneNumbers.get(updateTelephoneNumber.getCustomerId());
        Optional<TelephoneNumber> telephoneNumberOptional = telephoneNumbers.stream().
                filter(x -> x.getId() == updateTelephoneNumber.getId()).findFirst();
        if (telephoneNumberOptional.isPresent()){
            TelephoneNumber result = telephoneNumberOptional.get();
            result.setNumber(updateTelephoneNumber.getNumber());
            return result;
        }
        throw new IllegalAccessException("Unknown");
    }
}
