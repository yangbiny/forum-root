package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.Reply;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: forum-root
 * @description: 博客留言信息
 * @author: Impassive
 * @create: 2019-05-24 17:06
 **/
@Mapper
@Repository
public interface ReplyDao {

    /**
     * 查询指定博客ID下的讨论信息
     *
     * @param id 博客ID
     * @return 讨论信息集合
     */
    @Select("select reply.id replyId,articleId,userName,time,content,avatar,replyTo from userInfo,reply where nickName = userName " +
            "and articleId = #{id}")
    @Results(id = "reply", value = {
            @Result(property = "id", column = "replyId"),
            @Result(property = "articleId", column = "articleId"),
            @Result(property = "userName", column = "userName"),
            @Result(property = "time", column = "time"),
            @Result(property = "content", column = "content"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "replyTo", column = "replyTo")
    })
    List<Reply> list(@Param("id") Integer id);

    /**
     * 添加一个新的讨论信息
     *
     * @param reply 讨论信息
     * @return 操作结果
     */
    @Insert("insert into reply set articleId = #{articleId},userName = #{userName}," +
            "time = #{time},content = #{content},replyTo = #{replyTo}")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer add(Reply reply);

    /**
     * 统计讨论次数
     *
     * @return 讨论次数
     */
    @Select("select count(*) from reply")
    int replyCount();

    /**
     * 统计不同分类下的博客的讨论数
     * （借助博客ID存储讨论次数，用户名存储分类名）
     *
     * @return 分类名称和对应的讨论次数
     */
    @Select("select sort.name sortName,count(sort.id) sortId from reply,sort,article where " +
            "article.id = reply.articleId and article.sortId = sort.id GROUP BY sortName")
    @Results(id = "replies", value = {
            @Result(property = "articleId", column = "sortId"),
            @Result(property = "userName", column = "sortName"),
    })
    List<Reply> maxReplyCount();
}
