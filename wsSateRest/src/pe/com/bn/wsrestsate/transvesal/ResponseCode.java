package pe.com.bn.wsrestsate.transvesal;

public enum ResponseCode {
    SUCCESS("0000", "Éxito en el proceso"),
    UNEXPECTED_ERROR("9999", "Error inesperado"),
    NO_MOVEMENTS_FOUND("4444", "No se encontraron movimientos"),
    OTHER_ERROR("1111", "Otro error");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
