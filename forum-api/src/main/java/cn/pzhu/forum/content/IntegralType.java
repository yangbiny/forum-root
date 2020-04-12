package cn.pzhu.forum.content;

/**
 * @author impassivey
 */

public enum IntegralType {

  /**
   * 登录获取,一个积分
   */
  LOGIN,

  /**
   * 发表博客获取，五个积分
   */
  PUB_ARTICLE,

  /**
   * 上传文件被下载获取，有用户觉定
   */
  DOWN_FILES,

  /**
   * 被采纳，由用户决定
   */
  ADOPTION,

  /**
   * 发布求助，有用户被采纳
   */
  HELP;


}
