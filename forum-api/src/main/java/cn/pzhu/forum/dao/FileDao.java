package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.FileInfo;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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


  /**
   * 根据用户ID查询文件信息
   *
   * @param userId 用户ID
   * @return 文件信息的集合
   */
  @Select("select id,userId,path,time,size,title,introduction,integral_num,down_num from files where userId = #{userId}")
  @Results(id = "fileInfos", value = {
      @Result(id = true, column = "id", property = "id"),
      @Result(column = "userId", property = "userId"),
      @Result(column = "path", property = "path"),
      @Result(column = "time", property = "time"),
      @Result(column = "size", property = "size"),
      @Result(column = "title", property = "title"),
      @Result(column = "introduction", property = "introduction"),
      @Result(column = "integral_num", property = "integral"),
      @Result(column = "down_num", property = "downNum")
  })
  List<FileInfo> queryFileInfosByUserId(@Param("userId") String userId);
}
