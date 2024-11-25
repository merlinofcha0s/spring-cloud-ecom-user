package fr.plb.ecom_user.resource.dto;

import fr.plb.ecom_user.entity.UserEntity;
import fr.plb.ecom_user.vo.UserAddress;
import fr.plb.ecom_user.vo.UserAddressBuilder;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record RestUser(UUID publicId,
                       String firstName,
                       String lastName,
                       String email,
                       String imageUrl,
                       Set<String> authorities,
                       UserAddress address) {

    public static RestUser from(UserEntity user) {
        fr.plb.ecom_user.resource.dto.RestUserBuilder restUserBuilder = fr.plb.ecom_user.resource.dto.RestUserBuilder.restUser();

        if (user.getAddressStreet() != null) {
            UserAddress userAddress = UserAddressBuilder.userAddress()
                    .street(user.getAddressStreet())
                    .city(user.getAddressCity())
                    .country(user.getAddressCountry())
                    .zipCode(user.getAddressZipCode())
                    .build();
            restUserBuilder.address(userAddress);
        }

        if (user.getImageURL() != null) {
            restUserBuilder.imageUrl(user.getImageURL());
        }

        return restUserBuilder
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .publicId(user.getPublicId())
                .authorities(RestAuthority.fromSet(user.getAuthorities()))
                .build();
    }
}
