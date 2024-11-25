package fr.plb.ecom_user.resource;

import fr.plb.ecom_user.resource.dto.RestUser;
import fr.plb.ecom_user.service.UserService;
import fr.plb.ecom_user.vo.UserAddressToUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersResource {

    private final UserService userService;

    public UsersResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update-address")
    public ResponseEntity<Void> updateAddress(UserAddressToUpdate userAddressToUpdate) {
        userService.updateAddress(userAddressToUpdate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-by-public-ids")
    public ResponseEntity<List<RestUser>> getByPublicId(@RequestParam("publicIds") String publicIdsRaw) {
        Set<UUID> publicIds = Arrays.stream(publicIdsRaw.split(","))
                .map(UUID::fromString)
                .collect(Collectors.toSet());
        List<RestUser> usersByPublicId = userService.getUsersByPublicId(publicIds)
                .stream().map(RestUser::from).toList();
        return ResponseEntity.ok(usersByPublicId);
    }
}
