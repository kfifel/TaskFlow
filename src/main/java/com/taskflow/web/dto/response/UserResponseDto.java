package com.taskflow.web.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
    private Integer numberOfChangeTokens;
    private LocalDate toDoubleTokenChaneDate;
    private boolean hasDeleteToken;
    private List<String> authorities;
}
