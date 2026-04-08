package com.devteria.hello_spring_boot.Service.user;

import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Request.CreateUserRequest;
import com.devteria.hello_spring_boot.Request.UpdateUserRequest;
import com.devteria.hello_spring_boot.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserService  {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request,Long userId);
    void deleteUser(Long id);
    User getAuthenticatedUser();
    UserDto getUserDtoById(Long userId); // Thêm hàm này
    UserDto convertUserToDto(User user);

}
