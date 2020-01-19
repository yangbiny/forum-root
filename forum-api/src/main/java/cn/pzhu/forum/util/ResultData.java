package cn.pzhu.forum.util;

import lombok.Data;

/**
 * @program: forum-root
 * @description: 结果集
 * @author: Impassive
 * @create: 2019-05-24 21:20
 **/
@Data
public class ResultData {

    /**
     * 错误码
     */
    Integer code;

    /**
     * 错误信息
     */
    String message;
}
