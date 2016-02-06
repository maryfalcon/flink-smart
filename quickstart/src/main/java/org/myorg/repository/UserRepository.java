package org.myorg.repository;

import org.myorg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Serjik
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
