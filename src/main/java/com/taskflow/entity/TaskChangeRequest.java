package com.taskflow.entity;

import com.taskflow.entity.enums.StatusRequest;
import com.taskflow.entity.enums.TokenType;
import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateRequest;

    @Enumerated(EnumType.STRING)
    private StatusRequest status;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @OneToOne
    private Task task;

    private Long oldOwnerId;
}
