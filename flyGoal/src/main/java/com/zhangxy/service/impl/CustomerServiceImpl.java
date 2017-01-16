/*
 * Copyright 2011-2014 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.zhangxy.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQuery;
import com.zhangxy.annotation.TestMy;
import com.zhangxy.entity.Customer;
import com.zhangxy.entity.QCustomer;
import com.zhangxy.repository.CustomerRepository;
import com.zhangxy.service.CustomerService;

/**
 * Plain JPA implementation of {@link CustomerService}.
 *
 * @author Oliver Gierke
 */
@Repository
@SuppressWarnings("unchecked")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    static private final String CUSTOMERS_REGION = "customers";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CustomerRepository res;

    /*
     * (non-Javadoc)
     * @see
     * example.springdata.jpa.showcase.before.CustomerService#findById(java.lang
     * .Long)
     */
    @Override
    public Customer findById(final Long id) {
        return res.findOne(id);
    }

    /*
     * (non-Javadoc)
     * @see example.springdata.jpa.showcase.before.CustomerService#findAll()
     */
    @Override
    @TestMy
    public List<Customer> findAll() {
        final QCustomer qCustomer = QCustomer.customer;
        final JPAQuery<Customer> query = new JPAQuery<>(em);
        //        final JPQLQuery query = querydsl.createQuery();
        return query.from(qCustomer).fetch();
        //        return this.em.createQuery("select c from Customer c", Customer.class).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see example.springdata.jpa.showcase.before.CustomerService#findAll(int,
     * int)
     */
    @Override
    public List<Customer> findAll(final int page, final int pageSize) {

        final TypedQuery<Customer> query = em.createQuery("select c from Customer c", Customer.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    /*
     * (non-Javadoc)
     * @see example.springdata.jpa.showcase.before.CustomerService#save(example.
     * springdata.jpa.showcase.core.Customer)
     */
    @Override
    @Transactional
    public Customer save(final Customer customer) {

        // Is new?
        if (customer.getId() == null) {
            em.persist(customer);
            return customer;
        } else {
            return em.merge(customer);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * example.springdata.jpa.showcase.before.CustomerService#findByLastname(
     * java.lang.String, int, int)
     */
    @Override
    public List<Customer> findByLastname(final String lastname, final int page, final int pageSize) {

        final TypedQuery<Customer> query =
                em.createQuery("select c from Customer c where c.lastname = ?1", Customer.class);

        query.setParameter(1, lastname);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public Customer createCustomer(final String firstName, final String lastName, final Date signupDate) {
        final Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSignupDate(signupDate);
        em.persist(customer);
        return customer;
    }

    @Override
    public Collection<Customer> search(final String name) {
        final String sqlName = ("%" + name + "%").toLowerCase();
        final String sql =
                "select c.* from customer c where (LOWER( c.first_name ) LIKE :fn OR LOWER( c.last_name ) LIKE :ln)";
        return em.createNativeQuery(sql, Customer.class)
                .setParameter("fn", sqlName)
                .setParameter("ln", sqlName)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return em.createQuery("SELECT * FROM " + Customer.class.getName()).getResultList();
    }

    @Override
    @Cacheable(CustomerServiceImpl.CUSTOMERS_REGION)
    @Transactional(readOnly = true)
    public Customer getCustomerById(final Long id) {
        return em.find(Customer.class, id);
    }

    @CacheEvict(CustomerServiceImpl.CUSTOMERS_REGION)
    public void deleteCustomer(final Long id) {
        final Customer customer = getCustomerById(id);
        em.remove(customer);
    }

    @Override
    @CacheEvict(value = CustomerServiceImpl.CUSTOMERS_REGION, key = "#id")
    public void updateCustomer(final Long id, final String fn, final String ln, final Date birthday) {
        final Customer customer = getCustomerById(id);
        customer.setLastName(ln);
        customer.setSignupDate(birthday);
        customer.setFirstName(fn);
        //sessionFactory.getCurrentSession().update(customer);
        em.merge(customer);
    }
}
