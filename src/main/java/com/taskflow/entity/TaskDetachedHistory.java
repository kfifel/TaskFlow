package com.taskflow.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetachedHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comments;

    @OneToOne
    private Task task;

    @CreationTimestamp
    private LocalDateTime dateDetached;

    @ManyToOne
    private User detachedBy;
}
