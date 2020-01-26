package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.dao.FileDao;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.FileService;
import javax.annotation.Resource;
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

    return result != null && result > 1;
  }
}
