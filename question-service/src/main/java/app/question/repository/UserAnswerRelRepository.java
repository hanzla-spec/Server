package app.question.repository;

import app.question.entity.UserAnswerRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnswerRelRepository extends JpaRepository<UserAnswerRel, String> {
    @Query(value = "select * from USER_ANSWER_REL where USER_ID = :userId and ANSWER_ID =:answerId", nativeQuery = true)
    Optional<UserAnswerRel> findUserAnswerRel(@Param(value = "userId") String userId, @Param(value = "answerId") String answerId);
}
