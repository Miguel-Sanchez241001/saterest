package pe.com.bn.wsrestsate.transvesal.exception;

import pe.com.bn.wsrestsate.transvesal.ResponseCode;

public class NoMovementsFoundException extends ApplicationException {
    public NoMovementsFoundException() {
        super(ResponseCode.NO_MOVEMENTS_FOUND);
    }
}