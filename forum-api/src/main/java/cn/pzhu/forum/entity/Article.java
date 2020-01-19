package cn.pzhu.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 文章实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Article implements Serializable {

    private static final long serialVersionUID = -7324263430976483432L;

    private Integer id;
    private String title;
    private String userName;
    private String time;
    private Integer sortId;
    private String context;
    private String contextMd;
    private Integer readNumber;
    private Integer top;
    private String principal;

    private Sort sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", time='" + time + '\'' +
                ", sortId=" + sortId +
                ", readNumber=" + readNumber +
                ", top=" + top +
                ", principal='" + principal + '\'' +
                ", sort=" + sort +
                '}';
    }
}
