package cn.pzhu.forum.content;

import lombok.Getter;

/**
 * 文章状态信息
 *
 * @author impassivey
 */
@Getter
public enum ArticleStatus {

  /**
   * 审核未通过
   */
  REJECT(-1),

  /**
   * 待审核
   */
  PENDING(0),

  /**
   * 正常状态
   */
  NORMAL(1),

  /**
   * 已删除
   */
  DELETE(2);

  private Integer code;

  ArticleStatus(Integer code) {
    this.code = code;
  }
}
