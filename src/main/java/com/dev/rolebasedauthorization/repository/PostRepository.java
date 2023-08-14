package com.dev.rolebasedauthorization.repository;

import com.dev.rolebasedauthorization.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
