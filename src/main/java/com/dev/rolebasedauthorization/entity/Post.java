package com.dev.rolebasedauthorization.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
@ToString
public class Post {
    @Id
    @GeneratedValue
    private Long postId;
    private String subject;
    private String description;
    private String username;

    @Enumerated(EnumType.STRING)
    private PostStatus status;
}
