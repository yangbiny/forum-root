package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.FileInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author impassivey
 */
@Mapper
@Repository
public interface FileDao {


  /**
   * 新增文件信息
   *
   * @param fileInfo 文件信息
   * @return 返回大于1时代表添加成功
   */
  @Insert("insert into files set userId = #{userId}, path = #{path}, time = now(), size = #{size}, "
      + "title = #{title}, introduction = #{introduction}, integral_num = #{integral}")
  Integer addFile(FileInfo fileInfo);


}
