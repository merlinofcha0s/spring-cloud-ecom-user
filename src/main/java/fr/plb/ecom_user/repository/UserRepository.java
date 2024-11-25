package fr.plb.ecom_user.repository;

import fr.plb.ecom_user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findOneByEmailIgnoreCase(String email);

  List<UserEntity> findByPublicIdIn(Set<UUID> publicIds);

  Optional<UserEntity> findOneByPublicId(UUID publicId);

  @Modifying
  @Query("UPDATE UserEntity  user " +
    "SET user.addressStreet = :street, user.addressCity = :city, " +
    " user.addressCountry = :country, user.addressZipCode = :zipCode " +
    "WHERE user.publicId = :userPublicId")
  void updateAddress(UUID userPublicId, String street, String city, String country, String zipCode);
}
