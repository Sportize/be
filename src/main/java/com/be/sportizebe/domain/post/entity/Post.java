package com.be.sportizebe.domain.post.entity;

import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @Column(nullable = false)
    private boolean isAnonymous = false; // false로 설정하여 기본값은 익명이 아님

    private String imgUrl; // 추후 리스트로 변경

    @Enumerated(EnumType.STRING)
    private PostProperty property; // 게시판 속성(종목 별로)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    public void update(String title, String content, String imgUrl) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }
}
