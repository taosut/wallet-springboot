package org.blockchain.wallet.service.impl;

import com.google.common.base.Preconditions;
import org.blockchain.wallet.constant.Constant;
import org.blockchain.wallet.constant.ErrorMessage;
import org.blockchain.wallet.entity.User;
import org.blockchain.wallet.mapper.UserMapper;
import org.blockchain.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User createUser(User user) {
        User userDto = new User();

        if(user.getUsername() != null) {
            userDto.setUsername(user.getUsername());
            userDto = userMapper.findAccount(userDto);
            if(userDto != null && userDto.getRole() != null && userDto.getRole().equals(Constant.GUEST)) {

            } else {
                Preconditions.checkArgument((userDto == null), ErrorMessage.USERNAME_EXIST);
            }
        }

        if(user.getEmail() != null) {
            userDto = new User();
            userDto.setEmail(user.getEmail());
            userDto = userMapper.findAccount(userDto);
            if(userDto != null && userDto.getRole() != null && userDto.getRole().equals(Constant.GUEST)) {

            } else {
                Preconditions.checkArgument((userDto==null), ErrorMessage.EMAIL_EXIST);
            }
        }

        if(user.getPhone() != null) {
            userDto = new User();
            userDto.setPhone(user.getPhone());
            userDto = userMapper.findAccount(userDto);
            if(userDto != null && userDto.getRole() != null && userDto.getRole().equals(Constant.GUEST)) {

            } else {
                Preconditions.checkArgument((userDto == null), ErrorMessage.PHONE_EXIST);
            }
        }

        userMapper.insert(user);
        user = findUserById(user.getId());
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> findUserBySelective(User user) {
        return userMapper.selectBySelective(user);
    }

    @Override
    public User login(User userDto) {

        User user = userMapper.findAccount(userDto);
        Preconditions.checkNotNull(user, ErrorMessage.ACCOUNT_NOT_FOUND);
        Preconditions.checkArgument((user.getPassword().equals(userDto.getPassword())), ErrorMessage.ACCOUNT_PASSWORD_ERROR);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        user = findUserById(user.getId());
        return user;
    }
}
