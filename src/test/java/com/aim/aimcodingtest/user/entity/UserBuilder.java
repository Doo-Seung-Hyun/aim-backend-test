package com.aim.aimcodingtest.user.entity;

import com.aim.aimcodingtest.user.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class UserBuilder extends User {
    private Long id = 1L;
    private String username = "test-user";
    private String password = "testPassword1!";
    private String name = "홍길동";
    private Role role = Role.USER;
    private String email = "testemail@test.com";
    private String phoneNumber = "01012345678";
    private String address = "서울시 동작구";

    public static UserBuilder builder() {
        return new UserBuilder();
    }
    public User build() {
        User user = new User();
        try {
            user.setId(id);
            setField(user, "username", username);
            setField(user, "password", password);
            setField(user, "name", name);
            setField(user, "role", role);
            setField(user, "email", email);
            setField(user, "phoneNumber", phoneNumber);
            setField(user, "address", address);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
        return user;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = User.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }
}