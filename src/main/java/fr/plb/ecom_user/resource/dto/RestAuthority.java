package fr.plb.ecom_user.resource.dto;

import fr.plb.ecom_user.entity.AuthorityEntity;
import org.jilt.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record RestAuthority(String name) {

  public static Set<String> fromSet(Set<AuthorityEntity> authorities) {
    return authorities.stream()
      .map(AuthorityEntity::getName)
      .collect(Collectors.toSet());
  }

}
