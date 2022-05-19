package app.question.repository;

import app.question.entity.VQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VQuestionRepository extends JpaRepository<VQuestion, String> {
}
