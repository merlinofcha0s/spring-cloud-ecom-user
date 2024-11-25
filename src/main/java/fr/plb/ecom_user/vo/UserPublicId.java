package fr.plb.ecom_user.vo;

import fr.plb.ecom_user.shared.error.domain.Assert;

import java.util.UUID;

public record UserPublicId(UUID value) {

  public UserPublicId {
    Assert.notNull("value", value);
  }
}
