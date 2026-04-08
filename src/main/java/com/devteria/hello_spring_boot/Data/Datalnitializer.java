package com.devteria.hello_spring_boot.Data;
import com.devteria.hello_spring_boot.Entity.Role;
import com.devteria.hello_spring_boot.Entity.User;

import com.devteria.hello_spring_boot.Respository.RoleRepository;
import com.devteria.hello_spring_boot.Respository.UserRepository;
import com.devteria.hello_spring_boot.Service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
    @RequiredArgsConstructor
    public class Datalnitializer  implements ApplicationListener<ApplicationReadyEvent> {

        private final UserRepository userRepository;
        private final ICartService cartService;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;

        @Override
        @Transactional
        public void onApplicationEvent(ApplicationReadyEvent event) {
            Set<String> defaulRoles=Set.of("ROLE_ADMIN","ROLE_USER");
            createDefaultRoleIfNotExits(defaulRoles);
            createDefaultAdminIfNotExits();
            createDefaultUserIfNotExits();
        }

        private void createDefaultUserIfNotExits() {
            Role userRole=roleRepository.findByRoleName("ROLE_USER").get();
            for (int i = 1; i <= 5; i++) {
                String defaultEmail = "user" + i + "@email.com";

                // Kiểm tra nếu email đã tồn tại thì bỏ qua
                if (userRepository.existsByEmail(defaultEmail)) {
                    continue;
                }

                User user = new User();
                user.setFirstName("The User");
                user.setLastName("User " + i);
                user.setEmail(defaultEmail);
                user.setPassWord(passwordEncoder.encode("123456")); // Lưu ý: Trong thực tế nên dùng PasswordEncoder
                user.setRoles(Set.of(userRole));
                User savedUser = userRepository.save(user);
                cartService.initializeNewCart(savedUser); // Gọi hàm này để tạo giỏ hàng trống cho user
                System.out.println("Default user " + i + " created successfully.");
            }
        }

    private void createDefaultAdminIfNotExits() {
        Role adminRole=roleRepository.findByRoleName("ROLE_ADMIN").get();
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "admin" + i + "@email.com";

            // Kiểm tra nếu email đã tồn tại thì bỏ qua
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin " + i);
            user.setEmail(defaultEmail);
            user.setPassWord(passwordEncoder.encode("123456")); // Lưu ý: Trong thực tế nên dùng PasswordEncoder
            user.setRoles(Set.of(adminRole));
            User savedUser = userRepository.save(user);
            cartService.initializeNewCart(savedUser); // Gọi hàm này để tạo giỏ hàng trống cho user
            System.out.println("Default Admin " + i + " created successfully.");
        }
    }
    private  void createDefaultRoleIfNotExits(Set<String> roles){
            roles.stream().filter(role->roleRepository.findByRoleName(role).isEmpty())
            .map(Role::new).forEach(roleRepository::save);
    }
    }
