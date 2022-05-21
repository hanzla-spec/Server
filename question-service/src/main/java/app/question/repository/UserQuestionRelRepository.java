package app.question.repository;

import app.question.entity.UserQuestionRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQuestionRelRepository extends JpaRepository<UserQuestionRel, String> {
    @Query(value = "select * from USER_QUESTION_REL where USER_ID = :userId and QUESTION_ID = :questionId",nativeQuery = true)
    Optional<UserQuestionRel> findUserQuestionVoteRel(@Param(value = "userId") String userId,@Param(value = "questionId") String questionId);
}
