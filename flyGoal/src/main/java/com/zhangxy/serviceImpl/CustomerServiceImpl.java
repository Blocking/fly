/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhangxy.serviceImpl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.zhangxy.entity.Customer;
import com.zhangxy.entity.QCustomer;
import com.zhangxy.entity.QUser;
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
	
	@PersistenceContext private EntityManager em;

	/*
	 * (non-Javadoc)
	 * @see example.springdata.jpa.showcase.before.CustomerService#findById(java.lang.Long)
	 */
	@Override
	public Customer findById(Long id) {
		return em.find(Customer.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see example.springdata.jpa.showcase.before.CustomerService#findAll()
	 */
	@Override
	public List<Customer> findAll() {
		QCustomer qCustomer = QCustomer.customer;
		QUser qUser = QUser.user;
		 PathBuilder<Customer> builder = new PathBuilderFactory().create(Customer.class);
		Querydsl querydsl = new Querydsl(em, builder);
		JPQLQuery<Customer> query = querydsl.createQuery();
		query.from(qCustomer);
		query.leftJoin(qUser).on(qCustomer.id.eq(qUser.id));
		List<Customer> customers = query.fetch();
		customers.stream().forEach(p->{
			System.err.println(p.getFirstName());
		});
		return em.createQuery("select c from Customer c", Customer.class).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see example.springdata.jpa.showcase.before.CustomerService#findAll(int, int)
	 */
	@Override
	public List<Customer> findAll(int page, int pageSize) {

		TypedQuery<Customer> query = em.createQuery("select c from Customer c", Customer.class);

		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see example.springdata.jpa.showcase.before.CustomerService#save(example.springdata.jpa.showcase.core.Customer)
	 */
	@Override
	@Transactional
	public Customer save(Customer customer) {

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
	 * @see example.springdata.jpa.showcase.before.CustomerService#findByLastname(java.lang.String, int, int)
	 */
	@Override
	public List<Customer> findByLastname(String lastname, int page, int pageSize) {

		TypedQuery<Customer> query = em.createQuery("select c from Customer c where c.lastname = ?1", Customer.class);

		query.setParameter(1, lastname);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}
	
	
	public Customer createCustomer(String firstName, String lastName, Date signupDate) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSignupDate(signupDate);
        em.persist(customer);
        return customer;
    }

    public Collection<Customer> search(String name) {
        String sqlName = ("%" + name + "%").toLowerCase();
        String sql = "select c.* from customer c where (LOWER( c.first_name ) LIKE :fn OR LOWER( c.last_name ) LIKE :ln)";
        return em.createNativeQuery(sql, Customer.class)
                .setParameter("fn", sqlName)
                .setParameter("ln", sqlName)
                .getResultList();
    }


    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return em.createQuery("SELECT * FROM " + Customer.class.getName()).getResultList();
    }


    @Cacheable(CUSTOMERS_REGION)
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return em.find(Customer.class, id);
    }

    @CacheEvict(CUSTOMERS_REGION)
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        em.remove(customer);
    }

    @CacheEvict(value = CUSTOMERS_REGION, key = "#id")
    public void updateCustomer(Long id, String fn, String ln, Date birthday) {
        Customer customer = getCustomerById(id);
        customer.setLastName(ln);
        customer.setSignupDate(birthday);
        customer.setFirstName(fn);
        //sessionFactory.getCurrentSession().update(customer);
        em.merge(customer);
    }
}
