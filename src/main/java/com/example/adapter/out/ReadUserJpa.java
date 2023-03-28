package com.example.adapter.out;

import com.example.adapter.out.mapper.UserOutMapper;
import com.example.application.port.out.ReadUserStore;
import com.example.domain.UserDomain.UserInfo;
import com.example.infra.database.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReadUserJpa implements ReadUserStore {
  private final UserRepository userRepository;
  private final UserOutMapper userOutMapper;

  @Override
  @Transactional(readOnly = true)
  public Optional<UserInfo> findUserByUserId(long memberId) {
    return userRepository.findById(memberId)
      .map(userOutMapper::toUserInfo);
  }
}
