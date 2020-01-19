package cn.pzhu.forum.content;

/**
 * 身份信息实体类
 */
public enum Identify {

    /**
     * 普通用户
     */
    USER(0, "普通用户"),

    /**
     * 管理员用户
     */
    ADMIN(1, "管理员"),

    /**
     * 已经被删除的用户
     */
    DEAD(-1, "已过期用户");

    private Integer identify;
    private String msg;

    Identify(Integer identify, String msg) {
        this.identify = identify;
        this.msg = msg;
    }

    public Integer getIdentify() {
        return identify;
    }

    public String getMsg() {
        return msg;
    }
}
