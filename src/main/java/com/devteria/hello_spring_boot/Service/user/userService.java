package com.devteria.hello_spring_boot.Service.user;

import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Exceptions.AlreadyExistsException;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Request.CreateUserRequest;
import com.devteria.hello_spring_boot.Request.UpdateUserRequest;
import com.devteria.hello_spring_boot.Respository.UserRepository;
import com.devteria.hello_spring_boot.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class userService implements IUserService {
    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new ResoureeNotFoundException("not found ID"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request).filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req-> {
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassWord(passwordEncoder.encode(request.getPassWord()));
                    return userRepository.save(user);
                })
.orElseThrow(() -> new AlreadyExistsException("Oops!" +request.getEmail() +" already exists!"));    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(c->{
            c.setFirstName(request.getFirstName());
            c.setLastName(request.getLastName());
            return userRepository.save(c);
        }).orElseThrow(()->new ResoureeNotFoundException("Not found id")) ;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete,
                        ()->{throw new ResoureeNotFoundException("not found id");
                });
   }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        return userRepository.findByEmail(email);
    }

    @Transactional // Đảm bảo Session mở xuyên suốt hàm này
    @Override
    public UserDto getUserDtoById(Long userId) {
        // 1. Lấy Entity
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResoureeNotFoundException("not found ID"));

        // 2. Ép tải dữ liệu (Initialize) ngay tại đây
        if (user.getOrders() != null) user.getOrders().size();
        if (user.getCart() != null) user.getCart().getCartItems().size();

        // 3. Convert sang DTO và trả về
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
   @Override
    public UserDto convertUserToDto(User user){
       // Ép Hibernate tải dữ liệu trước khi map (để chắc chắn 100%)
       if (user.getOrders() != null) user.getOrders().size();
       if (user.getCart() != null) user.getCart().getCartItems().size();
        return  modelMapper.map(user,UserDto.class);
   }
}
