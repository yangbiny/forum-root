package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.FileInfo;

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
}
