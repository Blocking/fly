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
package com.zhangxy.serviceImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQuery;
import com.zhangxy.entity.QUser;
import com.zhangxy.entity.User;
import com.zhangxy.repository.UserRepository;
import com.zhangxy.service.UsersService;

@Repository
@Transactional
public class UsersServiceImpl implements UsersService {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user = this.getUserByUserName(username);
        } catch (final Exception e) {
            e.printStackTrace();
            this._log.error("根据用户名获取用户异常！！");
            throw new UsernameNotFoundException("方法异常");
        }
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        return user;
    }

    @Override
    public User getUserByUserName(final String username) {
        final QUser qUser = QUser.user;
        final JPAQuery<User> query = new JPAQuery<>(this.em);
        return query.select(qUser).where(qUser.username.eq(username)).fetchOne();
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User save(final User user) {
        return this.userRepository.save(user);
    }

}
