package com.lifesync.auth.dto;

import com.lifesync.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String fullName;
    private String email;
    private String role;

    public static UserSummary from(User user) {
        return new UserSummary(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
