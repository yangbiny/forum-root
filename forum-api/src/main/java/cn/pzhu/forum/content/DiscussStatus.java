package cn.pzhu.forum.content;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum DiscussStatus {

    /**
     * 初始状态，未采纳，刚创建
     */
    INIT(0),

    /**
     * 已采纳状态
     */
    ADOPTION(1);

    private final Integer code;

    DiscussStatus(Integer code) {
        this.code = code;
    }

    private static DiscussStatus valueOfCode(Integer status){
        if(status == null){
            return null;
        }
        for (DiscussStatus value : DiscussStatus.values()) {
            if(Objects.equals(value.code,status)){
                return value;
            }
        }
        return null;
    }
}
