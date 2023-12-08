package uk.co.cb.graphql.demo.controller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import uk.co.cb.graphql.demo.entity.QTelephoneNumberEntity;
import uk.co.cb.graphql.demo.entity.TelephoneNumberEntity;
import uk.co.cb.graphql.demo.model.TelephoneNumber;
import uk.co.cb.graphql.demo.model.UpdateTelephoneNumber;
import uk.co.cb.graphql.demo.repository.TelephoneNumberRepository;

@Controller
public class TelephoneNumberController {

    @Autowired
    private TelephoneNumberRepository telephoneNumberRepository;

    @Autowired
    private EntityManager entityManager;

    @SneakyThrows
    @MutationMapping
    public TelephoneNumber updateTelephoneNumber(@Argument UpdateTelephoneNumber updateTelephoneNumber){

        QTelephoneNumberEntity telephoneNumber = QTelephoneNumberEntity.telephoneNumberEntity;

        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        TelephoneNumberEntity updateable = factory.
                query().
                select(telephoneNumber).
                from(telephoneNumber).
                where(telephoneNumber.id.eq(updateTelephoneNumber.getId())).
                fetchOne();

        updateable.setNumber(updateTelephoneNumber.getNumber());
        updateable = telephoneNumberRepository.save(updateable);

        return TelephoneNumber.builder().
                id(updateable.getId()).
                number(updateable.getNumber()).
                build();
    }
}
