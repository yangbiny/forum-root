package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.Sort;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类信息数据库访问层
 * @author impassivey
 */
@Mapper
@Repository
public interface SortDao {

    /**
     * 查询所有的分类信息
     *
     * @return 分类信息列表
     */
    @Select("select id,name,rely,number from sort")
    List<Sort> list();

    /**
     * 获得指定的分类信息
     *
     * @param id 分类信息ID
     * @return 分类信息
     */
    @Select("select id,name,rely,number from sort where id = #{id}")
    Sort get(@Param("id") int id);

    /**
     * 更新分类信息
     *
     * @param sort 需要更新的分类信息
     * @return true表示更新成功，false表示更新失败
     */
    @Update("update sort set name = #{name} where id = #{id}")
    boolean update(Sort sort);

    /**
     * 删除分类信息
     *
     * @param id 分类ID
     * @return true表示删除成功，false表示删除失败
     */
    @Delete("delete from sort where id = #{id}")
    boolean delete(@Param("id") int id);

    /**
     * 添加分类ID
     *
     * @param sort 需要添加的分类信息
     * @return true表示添加成功，false表示添加失败
     */
    @Insert("insert into sort set name = #{name},rely = #{rely}")
    boolean add(Sort sort);

    /**
     * 添加一次博客，则修改一次分类的个数
     *
     * @param id 分类的ID
     * @return 操作结果
     */
    @Update("update sort set number = number + 1")
    boolean addSortNumber(int id);

    @Select("select id,name,rely,number from sort where rely is null")
    List<Sort> queryFirstLevelSort();

    @Select("select id,name,rely,number from sort where rely = #{id}")
    List<Sort> queryWithId(@Param("id") Integer id);
}
