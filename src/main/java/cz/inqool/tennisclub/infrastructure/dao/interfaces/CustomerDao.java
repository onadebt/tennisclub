package cz.inqool.tennisclub.infrastructure.dao.interfaces;

import cz.inqool.tennisclub.domain.model.Customer;

import java.util.Optional;

public interface CustomerDao extends GenericDao<Customer> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);

}
