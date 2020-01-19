package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @program: forum-root
 * @description: 点赞数据库访问层
 * @author: Impassive
 * @create: 2019-06-08 18:43
 **/
@Mapper
@Repository
public interface RecordDao {

    /**
     * 添加一个点赞信息
     *
     * @param record 点赞信息
     * @return 添加结果
     */
    @Insert("insert into record set username = #{userName},articlePrincipal = #{articlePrincipal}")
    boolean add(Record record);

    /**
     * 查询对应博客关键字下的点赞信息
     *
     * @param principal 博客信息
     * @return 记录数量
     */
    @Select("select count(*) from record where articlePrincipal = #{principal}")
    Integer list(@Param("principal") String principal);

    /**
     * 统计点赞次数
     *
     * @return 点赞次数
     */
    @Select("select count(*) from record")
    int recordCount();


}
