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
}
