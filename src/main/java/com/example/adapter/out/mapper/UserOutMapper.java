package com.example.adapter.out.mapper;

import com.example.domain.UserDomain.UserInfo;
import com.example.infra.database.User;
import org.springframework.stereotype.Component;

@Component
public class UserOutMapper {
  public UserInfo toUserInfo(User entity){
    return new UserInfo(entity);
  }
}
