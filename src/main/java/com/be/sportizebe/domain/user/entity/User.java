package com.be.sportizebe.domain.user.entity;

import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.global.common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자의 아이디(반드시 고유해야 함)

    @Column(nullable = false)
    @JsonIgnore // 응답 시 데이터를 json 형식으로 보낼때 이 부분은 보내지 않는다
    private String password;

    @Column(nullable = false)
    @JsonIgnore
    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private List<SportType> interestType; // 사용자 관심 종목

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>(); // 작성한 게시글 목록
}
