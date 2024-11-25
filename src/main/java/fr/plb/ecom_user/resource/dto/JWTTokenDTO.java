package fr.plb.ecom_user.resource.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JWTTokenDTO(@JsonProperty("id_token") String idToken) {
}
