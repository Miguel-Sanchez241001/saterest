package pe.com.bn.wsrestsate.transvesal.exception;

import pe.com.bn.wsrestsate.transvesal.ResponseCode;

public class ApplicationException extends Exception {
    private final ResponseCode responseCode;

    public ApplicationException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public ApplicationException(ResponseCode responseCode, String detailMessage) {
        super(detailMessage);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
