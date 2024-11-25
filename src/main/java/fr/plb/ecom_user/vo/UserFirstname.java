package fr.plb.ecom_user.vo;

import fr.plb.ecom_user.shared.error.domain.Assert;

public record UserFirstname(String value) {

  public UserFirstname {
    Assert.field("value", value).maxLength(255);
  }
}
