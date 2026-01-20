package com.be.sportizebe.domain.like.entity;

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
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "target_type", "target_id"})
})
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 좋아요 누른 사용자

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private LikeTargetType targetType; // POST 또는 COMMENT

    @Column(name = "target_id", nullable = false)
    private Long targetId; // 게시글 ID 또는 댓글 ID
}