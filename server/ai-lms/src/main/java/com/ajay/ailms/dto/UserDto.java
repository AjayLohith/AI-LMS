package com.ajay.ailms.dto;

import com.ajay.ailms.entity.type.Role;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String createdAt;

}
