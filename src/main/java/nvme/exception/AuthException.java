package nvme.exception;

public class AuthException extends java.lang.Exception {
    String detailMessage = null;
    Throwable cause;
    public AuthException() {
        cause = super.getCause();
    }

    public AuthException(java.lang.String s) {
        cause = super.getCause();
    }

}