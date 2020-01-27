package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.dao.FileDao;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.FileService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

  @Resource
  private FileDao fileDao;

  @Override
  public boolean addFile(FileInfo fileInfo) {

    Integer result = fileDao.addFile(fileInfo);

    return result != null && result > 0;
  }

  @Override
  public List<FileInfo> queryFileInfosByUserId(String userId) {
    List<FileInfo> fileInfos = fileDao.queryFileInfosByUserId(userId);
    if (CollectionUtils.isEmpty(fileInfos)) {
      return Collections.emptyList();
    }
    return fileInfos;
  }

  @Override
  public FileInfo queryFileInfoById(Integer id) {
    return fileDao.queryFileInfoById(id);
  }

  @Override
  public boolean deleteFileInfos(Integer id) {
    return fileDao.deleteFileInfo(id) > 0;
  }


}
