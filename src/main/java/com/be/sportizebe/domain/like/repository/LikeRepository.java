package com.be.sportizebe.domain.like.repository;

import com.be.sportizebe.domain.like.entity.Like;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 특정 사용자가 특정 대상에 좋아요 했는지 확인
    /**
 * Finds a Like for the specified user and target (type and ID).
 *
 * @return an Optional containing the Like if found, empty otherwise
 */
    Optional<Like> findByUserAndTargetTypeAndTargetId(User user, LikeTargetType targetType, Long targetId);

    /**
 * Determines whether a user has liked a specific target.
 *
 * @param user the user to check
 * @param targetType the type of the target (e.g., post, comment)
 * @param targetId the identifier of the target
 * @return `true` if a Like exists for the specified user and target, `false` otherwise
 */
    boolean existsByUserAndTargetTypeAndTargetId(User user, LikeTargetType targetType, Long targetId);

    /**
 * Get the number of likes for a specific target.
 *
 * @param targetType the type of the liked target (for example, POST or COMMENT)
 * @param targetId   the identifier of the target
 * @return           the count of likes for the specified target
 */
    long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId);

    /**
 * Delete a user's like for a specific target.
 *
 * Deletes any Like record that matches the provided user, target type, and target ID.
 *
 * @param user the user who created the like
 * @param targetType the type/category of the liked target
 * @param targetId the identifier of the liked target
 */
    void deleteByUserAndTargetTypeAndTargetId(User user, LikeTargetType targetType, Long targetId);
}