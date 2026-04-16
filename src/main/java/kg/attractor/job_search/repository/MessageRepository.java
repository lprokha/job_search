package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}