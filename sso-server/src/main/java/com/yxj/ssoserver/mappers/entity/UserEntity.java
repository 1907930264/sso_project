package com.yxj.ssoserver.mappers.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_user")
public class UserEntity extends BaseEntity {

    private Long id;

    private String loginName;

    private String password;

    private String nickName;

    private String email;

    private String phoneNumber;

    private String idCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginName=" + loginName +
                ", password=" + password +
                ", nickName=" + nickName +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", idCard=" + idCard +
                "}";
    }
}
