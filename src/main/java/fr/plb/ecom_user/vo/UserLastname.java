package fr.plb.ecom_user.vo;


import fr.plb.ecom_user.shared.error.domain.Assert;

public record UserLastname(String value) {

  public UserLastname {
    Assert.field("value", value).maxLength(255);
  }
}
