package org.blockchain.wallet.service;


import org.blockchain.wallet.entity.User;

import java.util.List;

/**
 * @author hxy
 */
public interface UserService {

    User createUser(User user);

    User findUserById(Integer id);

    List<User> findUserBySelective(User user);

    User login(User user);

    User updateUser(User user);

    User forgetPassword(User user);
}
