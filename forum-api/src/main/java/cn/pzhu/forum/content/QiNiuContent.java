package cn.pzhu.forum.content;

import lombok.Data;

import java.io.Serializable;

@Data
public class QiNiuContent implements Serializable {

    private static final long serialVersionUID = -7311393724244275688L;

    private String accessKey = "35oLM3Xp6p3tvyFdLHPMMHxNcFsrGA0hWxmH2z1H";
    private String secretKey = "c_8nlz5k3_OcwlLgp3qiWJ1ax9gokrhRSMaSjsa-";
    private String bucket = "impassive";
    private String path = "http://impassive.cdn.cone4.top";

}
