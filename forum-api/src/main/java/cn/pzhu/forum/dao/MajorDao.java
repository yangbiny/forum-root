package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.Major;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专业DAO，用于访问数据库
 */
@Mapper
@Repository
public interface MajorDao {

    /**
     * 查询所有的专业信息
     *
     * @return 专业信息列表
     */
    @Select("select id,name,schoolId from major")
    List<Major> list();

    /**
     * 添加分类信息
     *
     * @param major 分类信息
     * @return true表示添加成功，false表示添加失败
     */
    @Insert("insert into major(name,schoolId) values (#{name},#{schoolId})")
    boolean add(Major major);

    /**
     * 更新专业信息
     *
     * @param major 需要更新的专业信息
     * @return true表示更新成功，false表示更新失败
     */
    @Update("update major set name = #{name} where id = #{id}")
    boolean update(Major major);

    /**
     * 删除专业信息
     *
     * @param id 专业ID
     * @return true表示删除成功，false表示删除失败
     */
    @Delete("delete from major where id = #{id}")
    boolean delete(@Param("id") int id);

}
