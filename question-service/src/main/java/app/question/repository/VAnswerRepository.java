package app.question.repository;

import app.question.entity.VAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VAnswerRepository extends JpaRepository<VAnswer, String> {

    List<VAnswer> findByQuestionIdOrderByNoOfVotesDesc(String questionId);
}
