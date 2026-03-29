package com.devteria.hello_spring_boot.Data;
import com.devteria.hello_spring_boot.Entity.User;

import com.devteria.hello_spring_boot.Respository.UserRepository;
import com.devteria.hello_spring_boot.Service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

    @Component
    @RequiredArgsConstructor
    public class Datalnitializer  implements ApplicationListener<ApplicationReadyEvent> {

        private final UserRepository userRepository;
        private final ICartService cartService;

        @Override
        @Transactional
        public void onApplicationEvent(ApplicationReadyEvent event) {
            createDefaultUserIfNotExits();
        }

        private void createDefaultUserIfNotExits() {
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
                user.setPassWord("123456"); // Lưu ý: Trong thực tế nên dùng PasswordEncoder

                User savedUser = userRepository.save(user);
                cartService.initializeNewCart(savedUser); // Gọi hàm này để tạo giỏ hàng trống cho user
                System.out.println("Default user " + i + " created successfully.");
            }
        }
    }
