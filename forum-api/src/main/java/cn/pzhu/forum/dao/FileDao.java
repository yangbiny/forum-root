package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.FileInfo;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

  /**
   * 查询文件信息
   *
   * @param id 文件信息的ID
   * @return 文件的详细信息
   */
  @Select("select id,userId,path,time,size,title,introduction,integral_num,down_num from files where id = #{id}")
  @ResultMap("fileInfos")
  FileInfo queryFileInfoById(@Param("id") Integer id);

  /**
   * 删除ID指定的文件信息
   *
   * @param id 文件ID
   * @return 大于0 代表删除成功
   */
  @Delete("delete from files where id = #{id}")
  Integer deleteFileInfo(@Param("id") Integer id);


  /**
   * 增加下载次数
   *
   * @param id 文件ID
   * @return 大于0则代表成功
   */
  @Update("update files set down_num = down_num + 1 where id = #{id}")
  Integer incrDownNum(@Param("id") String id);

  /**
   * 获得文件信息
   *
   * @param start 开始查询的位置
   * @param limit 查询的条数
   * @return 信息集合
   */
  @Select("select id,userId,path,time,size,title,introduction,integral_num,down_num from files limit #{start},#{limit}")
  @ResultMap("fileInfos")
  List<FileInfo> getFileInfo(@Param("start") Integer start, @Param("limit") Integer limit);
}
