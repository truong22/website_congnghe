package com.devteria.hello_spring_boot.Security.User;

import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class shopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable( userRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return shopUserDetails.buildUserDetail(user);

    }
}
