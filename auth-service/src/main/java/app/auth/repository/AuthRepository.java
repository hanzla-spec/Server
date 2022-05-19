package app.auth.repository;

import app.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, String> {
    public Optional<User> findByUsername(String username);
}
