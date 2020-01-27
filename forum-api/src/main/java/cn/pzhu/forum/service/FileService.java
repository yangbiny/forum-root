package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.FileInfo;
import java.util.List;

/**
 * @author impassivey
 */
public interface FileService {

  /**
   * 添加文件的信息
   *
   * @param fileInfo 文件的详细信息
   * @return TRUE：添加成功
   */
  boolean addFile(FileInfo fileInfo);

  /**
   * 根据用户ID查询文件信息
   *
   * @param userId 用户ID
   * @return 上传文件的信息
   */
  List<FileInfo> queryFileInfosByUserId(String userId);

  /**
   * 根据文件信息的ID查询文件信息
   *
   * @param id 文件的ID
   * @return 文件信息的详细信息
   */
  FileInfo queryFileInfoById(Integer id);

  /**
   * 删除文件信息
   *
   * @param id 文件信息的ID
   * @return true代表删除成功
   */
  boolean deleteFileInfos(Integer id);
}
