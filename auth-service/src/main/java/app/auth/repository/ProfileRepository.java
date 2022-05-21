package app.auth.repository;

import app.auth.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    @Query(value = "SELECT * FROM PROFILE WHERE USER_ID = :userId", nativeQuery = true)
    Optional<Profile> findProfileFromUserId(@Param("userId") String userId);

}
