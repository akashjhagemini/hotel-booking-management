package com.akash.hotelbookingmanagement.repository;

import com.akash.hotelbookingmanagement.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
