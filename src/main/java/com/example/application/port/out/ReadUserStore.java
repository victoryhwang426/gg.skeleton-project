package com.example.application.port.out;

import com.example.domain.UserDomain.UserInfo;
import java.util.Optional;

public interface ReadUserStore {
  Optional<UserInfo> findUserByUserId(long memberId);
}
