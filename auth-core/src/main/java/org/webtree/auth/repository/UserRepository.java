package org.webtree.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webtree.auth.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
