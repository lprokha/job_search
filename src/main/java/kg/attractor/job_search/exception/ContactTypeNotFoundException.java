package kg.attractor.job_search.exception;

public class ContactTypeNotFoundException extends NotFoundException {

    public ContactTypeNotFoundException() {
        super("Тип контакта не найден");
    }
}