package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByRespondedApplicant_IdOrderByTimestampAsc(Integer respondedApplicantId);
}