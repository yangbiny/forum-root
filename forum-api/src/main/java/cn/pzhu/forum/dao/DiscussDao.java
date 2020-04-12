package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.DiscussDO;
import cn.pzhu.forum.entity.DiscussItemDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussDao {

    @Select("select id,userId,create_time,content,integral,status,title from discuss limit #{start},#{limit}")
    @Results(id = "discuss",value = {
            @Result(property = "id",column = "id"),
            @Result(property = "userId",column = "userId"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "content",column = "content"),
            @Result(property = "integral",column = "integral"),
            @Result(property = "status",column = "status"),
            @Result(property = "title",column = "title"),
    })
    List<DiscussDO> listDiscuss(@Param("start") Integer start, @Param("limit") Integer limit);

    @Insert("insert into discuss set userId = #{userId},content = #{content}," +
            "create_time = #{createTime},integral = #{integral},status = #{status},title = #{title}")
    Boolean addDiscuss(DiscussDO discussDO);

    @Select("select id,userId,create_time,content,status,discuss_id from discuss_info " +
            "where discuss_id = #{discussId} limit #{start},#{limit}")
    @Results(id = "discussItem",value = {
            @Result(property = "id",column = "id"),
            @Result(property = "userId",column = "userId"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "content",column = "content"),
            @Result(property = "status",column = "status"),
            @Result(property = "discussId",column = "discuss_id"),
    })
    List<DiscussItemDO> listDiscussItem(@Param("discussId") Integer discussId, @Param("start") Integer start, @Param("limit") Integer limit);

    @Select("select id,userId,create_time,content,integral,status,title from discuss where id = #{discussId}")
    @ResultMap("discuss")
    DiscussDO findDiscussById(@Param("discussId") Integer discussId);

    @Insert("insert into discuss_info set userId = #{userId},content = #{content}," +
            "create_time = #{createTime},discuss_id = #{discussId},status = #{status}")
    Boolean addDiscussReply(DiscussItemDO toDiscussReply);

    @Select("select id,userId,create_time,content,status,discuss_id from discuss_info " +
            "where id = #{id}")
    @ResultMap("discussItem")
    DiscussItemDO findDiscussItemById(@Param("id") Integer discussItemId);

    @Update("update discuss set status = #{status} where id = #{id}")
    Boolean updateDiscussStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Update("update discuss_info set status = #{status} where id = #{id}")
    Boolean updateDiscussItemStatus(@Param("id") Integer id, @Param("status") Integer status);
}
