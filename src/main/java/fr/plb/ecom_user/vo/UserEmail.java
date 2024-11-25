package fr.plb.ecom_user.vo;

import fr.plb.ecom_user.shared.error.domain.Assert;

public record UserEmail(String value) {

  public UserEmail {
    Assert.field("value", value).maxLength(255);
  }
}
