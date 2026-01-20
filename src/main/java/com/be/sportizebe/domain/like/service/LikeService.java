package com.be.sportizebe.domain.like.service;

import com.be.sportizebe.domain.like.dto.response.LikeResponse;
import com.be.sportizebe.domain.like.entity.LikeTargetType;
import com.be.sportizebe.domain.user.entity.User;

public interface LikeService {

  /**
 * Toggle a user's like on the specified target.
 *
 * Adds a like if the user has not liked the target yet; removes the like if it already exists.
 *
 * @param user the user performing the like toggle
 * @param targetType the type of entity being liked
 * @param targetId the identifier of the target entity
 * @return a LikeResponse representing the result of the toggle operation, including whether the target is now liked and the updated like count
 */
  LikeResponse toggleLike(User user, LikeTargetType targetType, Long targetId);

  /**
 * Determine whether the specified user has liked the given target.
 *
 * @param user the user whose like status is being checked
 * @param targetType the type of the target entity to check (e.g., post, comment)
 * @param targetId the identifier of the target entity
 * @return `true` if the user has liked the specified target, `false` otherwise
 */
  boolean isLiked(User user, LikeTargetType targetType, Long targetId);

  /**
 * Retrieve the number of likes for a specific target.
 *
 * @param targetType the type of the entity that can be liked (e.g., post, comment)
 * @param targetId   the identifier of the target entity
 * @return           the number of likes for the specified target, or 0 if none
 */
  long getLikeCount(LikeTargetType targetType, Long targetId);
}