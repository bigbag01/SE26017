package com.kubernetes.serviceMaster.model;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by bambihui on 2019/6/28.
 */
@Transactional
public interface UserDao extends CrudRepository<User,Long> {
    public User findById(long id);
}


//@Transactional
//public interface AccountDao extends CrudRepository<Account, Long> {
//
//    /**
//     * Return the account having the id or null if no user is found.
//     *
//     * @param id the account id.
//     */
//    public Account findById(long id);
//
//}