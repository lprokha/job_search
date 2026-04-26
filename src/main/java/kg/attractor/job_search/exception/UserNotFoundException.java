package kg.attractor.job_search.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("Пользователь не найден");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}