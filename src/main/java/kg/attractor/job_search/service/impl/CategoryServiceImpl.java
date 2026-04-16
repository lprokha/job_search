package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}