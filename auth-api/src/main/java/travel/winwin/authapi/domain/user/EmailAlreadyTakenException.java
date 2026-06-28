package travel.winwin.authapi.domain.user;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String email) {
        super("Email already taken: " + email);
    }
}