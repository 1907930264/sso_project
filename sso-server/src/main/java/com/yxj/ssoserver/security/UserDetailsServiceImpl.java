package com.yxj.ssoserver.security;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxj.ssoserver.constants.MessageConstant;
import com.yxj.ssoserver.mappers.UserMapper;
import com.yxj.ssoserver.mappers.entity.UserEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {

        UserEntity user = getUserEntity(loginName);

        if (user == null) {
            throw new UsernameNotFoundException(MessageConstant.USER_IS_NULL);
        }
        SecurityUser securityUser = new SecurityUser(user);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }

    private UserEntity getUserEntity(String loginName){
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginName(loginName);
        Wrapper<UserEntity> wrapper = new QueryWrapper<>(userEntity);
        return userMapper.selectOne(wrapper);
    }
}
