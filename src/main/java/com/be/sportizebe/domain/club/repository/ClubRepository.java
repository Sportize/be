package com.be.sportizebe.domain.club.repository;

import com.be.sportizebe.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
  boolean existsByName(String name);

    @Query("""
        SELECT c FROM Club c
        WHERE (:cursor IS NULL OR c.id < :cursor)
        ORDER BY c.id DESC
    """)
    List<Club> findClubsByCursor(@Param("cursor") Long cursor, Pageable pageable);

    @Query("""
        select c
        from ClubMember cm
        join cm.club c
        where cm.user.id = :userId
          and (:cursor is null or c.id < :cursor)
        order by c.id desc
    """)
    List<Club> findMyClubsByCursor(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);
}
