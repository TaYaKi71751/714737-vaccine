package nvme.exception;

public class HttpResponseException extends java.lang.Exception {
    String detailMessage = null;
    Throwable cause;
    public HttpResponseException (){
        cause = super.getCause();
    }
    public HttpResponseException (String detailMessage){
        cause = super.getCause();
        this.detailMessage = detailMessage;
    }
    public HttpResponseException (int responseCode){
        cause = super.getCause();
        this.detailMessage = "Server returned " + responseCode;
    }
    public HttpResponseException (okhttp3.Response response){
        cause = super.getCause();
        this.detailMessage = "Server returned " + response.code() + ", " + response.message();
    }
}
