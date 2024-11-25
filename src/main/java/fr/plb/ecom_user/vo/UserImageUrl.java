package fr.plb.ecom_user.vo;

import fr.plb.ecom_user.shared.error.domain.Assert;

public record UserImageUrl(String value) {

  public UserImageUrl {
    Assert.field("value", value).maxLength(1000);
  }
}
