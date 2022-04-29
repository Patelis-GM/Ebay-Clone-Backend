package silkroad.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silkroad.dtos.user.UserSecurityDTO;
import silkroad.dtos.user.request.UserSignUpDTO;
import silkroad.entities.Role;
import silkroad.entities.Roles;
import silkroad.entities.User;
import silkroad.exceptions.LoginException;
import silkroad.exceptions.SignUpException;
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserSecurityDTO> user = this.userRepository.findByUsername(username);

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
    public void signUpUser(UserSignUpDTO user) {

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

}
