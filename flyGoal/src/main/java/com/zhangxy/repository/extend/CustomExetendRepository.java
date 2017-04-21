package com.zhangxy.repository.extend;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<Customer> page(final Predicate expr, Pageable page) {
        final long count = this.selectFrom(QCustomer.customer).where(expr).fetchCount();
        final List<Customer> pageList = this.selectFrom(QCustomer.customer).where(expr)
                .offset(page.getOffset()).limit(page.getPageSize()).fetch();
        final PageImpl<Customer> pageImpl = new PageImpl<>(pageList, page, count);
        return pageImpl;
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
