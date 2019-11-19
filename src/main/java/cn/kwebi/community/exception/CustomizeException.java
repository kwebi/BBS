package cn.kwebi.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.code=errorCode.getCode();
        message=errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public CustomizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
