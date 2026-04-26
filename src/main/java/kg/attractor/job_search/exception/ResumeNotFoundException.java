package kg.attractor.job_search.exception;

public class ResumeNotFoundException extends NotFoundException {

    public ResumeNotFoundException() {
        super("Резюме не найдено");
    }
}