package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.DiscussDO;
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
}
