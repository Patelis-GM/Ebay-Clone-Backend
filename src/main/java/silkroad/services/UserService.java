package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.page.PageResponse;
import silkroad.dtos.user.UserMapper;
import silkroad.dtos.user.request.UserSecurityDetails;
import silkroad.dtos.user.request.UserRegistration;
import silkroad.dtos.user.response.UserBasicDetails;
import silkroad.dtos.user.response.UserCompleteDetails;
import silkroad.entities.Role;
import silkroad.entities.Roles;
import silkroad.entities.User;
import silkroad.entities.User_;
import silkroad.exceptions.LoginException;
import silkroad.exceptions.SignUpException;
import silkroad.exceptions.UserException;
import silkroad.repositories.UserRepository;
import silkroad.security.UserInformation;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserSecurityDetails> user = this.userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new LoginException(username, LoginException.LOGIN_USER_NOT_FOUND);

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (Objects.equals(user.get().getRole(), Roles.ADMIN.toString()))
            grantedAuthorities.add(new SimpleGrantedAuthority(Roles.ADMIN.toString()));

        else if (Objects.equals(user.get().getRole(), Roles.USER.toString()) && user.get().getApproved())
            grantedAuthorities.add(new SimpleGrantedAuthority(Roles.USER.toString()));

        return new UserInformation(user.get().getUsername(), user.get().getPassword(), user.get().getApproved(), grantedAuthorities);
    }

    @Transactional
    public void signUpUser(UserRegistration user) {

        if (this.userRepository.existsByUsername(user.getUsername()))
            throw new SignUpException(user.getUsername(), SignUpException.SIGNUP_USERNAME_EXISTS, HttpStatus.BAD_REQUEST);

        if (this.userRepository.existsByEmail(user.getEmail()))
            throw new SignUpException(user.getEmail(), SignUpException.SIGNUP_EMAIL_EXISTS, HttpStatus.BAD_REQUEST);


        User newUser = new User(user.getUsername(),
                this.bCryptPasswordEncoder.encode(user.getPassword()),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getTin(),
                user.getAddress(),
                new Role(Roles.USER.toString()));

        this.userRepository.save(newUser);
    }

    @Transactional
    public void approveUser(String username) {
        if (this.userRepository.updateApprovalStatusByUsername(username, true) == 0)
            throw new UserException(username, UserException.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public UserCompleteDetails getUser(String username) {

        Optional<User> optionalUser = this.userRepository.findWithAddressByUsername(username);

        if (optionalUser.isPresent())
            return this.userMapper.toUserCompleteDetails(optionalUser.get());

        else
            throw new UserException(username, UserException.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public PageResponse<UserBasicDetails> getUsersBasicDetails(Boolean approvalStatus, Integer pageIndex, Integer pageSize) {
        Specification<User> userSpecification = null;

        if (approvalStatus != null)
            userSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.APPROVED), approvalStatus);

        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
        Page<User> userPage = this.userRepository.findAll(userSpecification, pageRequest);
        return new PageResponse<>(this.userMapper.toUserBasicDetailsList(userPage.getContent()), userPage.getNumber() + 1, userPage.getTotalPages(), userPage.getTotalElements(), userPage.getNumberOfElements());
    }
}
