package fr.plb.ecom_user.service;

import fr.plb.ecom_user.entity.UserEntity;
import fr.plb.ecom_user.repository.UserRepository;
import fr.plb.ecom_user.vo.UserAddressToUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getUsersByPublicId(Set<UUID> publicIds) {
        return userRepository.findByPublicIdIn(publicIds);
    }

    @Transactional
    public void updateAddress(UserAddressToUpdate userAddressToUpdate) {
        userRepository.updateAddress(userAddressToUpdate.userPublicId().value(),
                userAddressToUpdate.userAddress().street(),
                userAddressToUpdate.userAddress().city(),
                userAddressToUpdate.userAddress().country(),
                userAddressToUpdate.userAddress().zipCode());
    }
}
