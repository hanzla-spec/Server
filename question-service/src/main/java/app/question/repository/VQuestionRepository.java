package app.question.repository;

import app.question.entity.VQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VQuestionRepository extends JpaRepository<VQuestion, String> {
}
