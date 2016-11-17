package com.zhangxy.repository.extend;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.zhangxy.entity.Customer;
import com.zhangxy.entity.QCustomer;

@Repository
@Transactional
public class CustomExetendRepository extends AbstractRepository<Customer> {
    
    public List<Customer> findAll(final Predicate expr) {
        return this.selectFrom(QCustomer.customer).where(expr).fetch();
    }
    
    public Customer save(Customer customer) {
        return persistOrMerge(customer);
    }
    
    public Customer findById(Long id) {
        return this.find(Customer.class,id);
    }
    
    public void saveO(Customer customer){
         persist(customer);
    }


    public List<Customer> all() {
        return selectFrom(QCustomer.customer).fetch();
    }
}
