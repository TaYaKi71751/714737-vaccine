package nvme.exception;

public class NotEligibleVaccineSelectException extends java.lang.Exception {
    String detailMessage = null;
    Throwable cause;
    public NotEligibleVaccineSelectException() {
        cause = super.getCause();
    }

    public NotEligibleVaccineSelectException(java.lang.String s) {
        cause = super.getCause();
    }

}
