package com.smartshopp.mapper;


import org.mapstruct.Mapper;
import com.smartshopp.dto.UserDTO;
import com.smartshopp.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
