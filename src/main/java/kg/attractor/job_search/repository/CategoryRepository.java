package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
