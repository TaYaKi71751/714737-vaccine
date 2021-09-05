package nvme.exception;

public class InvalidLogInException extends java.lang.Exception {
    String detailMessage = null;
    Throwable cause;
    public InvalidLogInException() {
        cause = super.getCause();
    }

    public InvalidLogInException(java.lang.String s) {
        cause = super.getCause();
    }

}