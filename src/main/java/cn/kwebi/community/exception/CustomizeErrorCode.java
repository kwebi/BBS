package cn.kwebi.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"问题不存在~"),
    TARGET_PARENT_NOT_FOUND(2002,"未选中问题或评论~"),
    NO_LOGIN(2003,"未登录，无法操作~"),
    SYSTEM_ERROR(2004,"service error");
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    private Integer code;

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
