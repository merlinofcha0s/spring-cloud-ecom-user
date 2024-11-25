package fr.plb.ecom_user.vo;

import fr.plb.ecom_user.shared.error.domain.Assert;

public record AuthorityName(String name) {

  public AuthorityName {
    Assert.field("name", name).notNull();
  }
}
