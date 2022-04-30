package silkroad.dtos.user;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import silkroad.dtos.user.response.UserBasicDetails;
import silkroad.dtos.user.response.UserCompleteDetails;
import silkroad.entities.User;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserCompleteDetails mapToUserCompleteDetails(User user);

    @Mapping(source = "role.name", target = "role")
    UserBasicDetails mapToUserBasicDetails(User user);

    List<UserBasicDetails> mapToUsersBasicDetails(List<User> users);

}
