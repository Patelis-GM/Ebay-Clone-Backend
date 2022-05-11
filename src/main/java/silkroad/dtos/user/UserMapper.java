package silkroad.dtos.user;

import org.mapstruct.*;
import silkroad.dtos.user.response.UserBasicDetails;
import silkroad.dtos.user.response.UserCompleteDetails;
import silkroad.entities.User;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserCompleteDetails toUserCompleteDetails(User user);

    @Mapping(source = "role.name", target = "role")
    UserBasicDetails toUserBasicDetails(User user);

    List<UserBasicDetails> toUserBasicDetailsList(List<User> users);

}
