package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.School;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学院信息数据库访问层
 */
@Mapper
@Repository
public interface SchoolDao {

    /**
     * 查询所有的学院信息
     *
     * @return 学院信息列表
     */
    @Select("select id,name from school")
    List<School> list();

    /**
     * 添加学院信息
     *
     * @param name 学院名称
     * @return true表示添加成功，false表示添加失败
     */
    @Insert("insert into school set name = #{name}")
    boolean add(@Param("name") String name);

    /**
     * 更新新学员信息
     *
     * @param school 需要更新的学院信息
     * @return true表示添加成功，false表示添加失败
     */
    @Update("update school set name = #{name} where id = #{id}")
    boolean update(School school);

    /**
     * 删除学院信息
     *
     * @param id 学院ID
     * @return true表示删除成功，false表示删除失败
     */
    @Delete("delete from school where id = #{id}")
    boolean delete(@Param("id") int id);
}
