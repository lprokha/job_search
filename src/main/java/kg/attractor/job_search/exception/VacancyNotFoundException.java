package kg.attractor.job_search.exception;

public class VacancyNotFoundException extends NotFoundException {

    public VacancyNotFoundException() {
        super("Вакансия не найдена");
    }
}