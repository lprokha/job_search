package kg.attractor.job_search.exception;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super("Категория не найдена");
    }
}