package cn.kwebi.community.enums;

public enum  CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    public Integer getType() {
        return type;
    }

    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
