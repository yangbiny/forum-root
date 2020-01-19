package cn.pzhu.forum.content;

public enum TopFlag {

    TOP(1, "置顶"),
    NORMAL(0, "未置顶");

    private Integer flag;
    private String message;

    TopFlag(Integer flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public Integer getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }
}
