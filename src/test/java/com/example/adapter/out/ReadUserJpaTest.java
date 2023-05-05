package com.example.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.UnitTest;
import com.example.adapter.out.mapper.UserOutMapper;
import com.example.domain.UserDomain.UserInfo;
import com.example.infra.database.User;
import com.example.infra.database.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ReadUserJpaTest extends UnitTest {

  @InjectMocks
  private ReadUserJpa readUserJpa;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserOutMapper userOutMapper;

  @Nested
  class findUserByUserId {

    @Test
    @DisplayName("find a user info by id")
    void test1() {
      User user = mock(User.class);
      when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

      UserInfo domain = mock(UserInfo.class);
      when(userOutMapper.toUserInfo(any())).thenReturn(domain);

      Optional<UserInfo> result =
          readUserJpa.findUserByUserId(1);

      verify(userRepository).findById(eq(1L));
      verify(userOutMapper).toUserInfo(eq(user));

      assertThat(result).isPresent();
      assertThat(result.get()).isEqualTo(domain);
    }
  }
}