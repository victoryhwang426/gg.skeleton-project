package com.example.domain;

import com.example.infra.database.User;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

public class UserDomain {
  @Getter
  @ToString
  public static class UserInfo {
    private final Long userId;
    private final String userName;

    public UserInfo(User entity){
      this.userId = entity.getUserId();
      this.userName = entity.getUserName();
    }
  }

  @Getter
  @ToString
  public static class BuyPurchaseCommand {
    private final long userId;
    private final List<Long> items;

    public BuyPurchaseCommand(long userId, List<Long> items){
      this.userId = userId;
      this.items = items;
    }
  }
}
