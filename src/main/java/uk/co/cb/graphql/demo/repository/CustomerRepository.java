package uk.co.cb.graphql.demo.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.graphql.data.GraphQlRepository;
import uk.co.cb.graphql.demo.entity.CustomerEntity;

@GraphQlRepository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long>,
        QuerydslPredicateExecutor<CustomerEntity> {
}
