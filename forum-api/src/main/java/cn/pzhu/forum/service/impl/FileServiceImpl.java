package cn.pzhu.forum.service.impl;

import cn.pzhu.forum.content.IntegralType;
import cn.pzhu.forum.content.QiNiuContent;
import cn.pzhu.forum.dao.FileDao;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.FileService;
import cn.pzhu.forum.service.IntegralService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author impassivey
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

  @Resource
  private FileDao fileDao;

  @Resource
  private IntegralService integralService;

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

  @Override
  public void incrDownNumById(String id) {
    fileDao.incrDownNum(id);
  }

  @Override
  public List<FileInfo> getFileInfo(Integer start, Integer limit) {
    List<FileInfo> fileInfos = fileDao.getFileInfo(start, limit);

    if (CollectionUtils.isEmpty(fileInfos)) {
      return Collections.emptyList();
    }

    return fileInfos;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String getDownUrl(String userId, FileInfo fileInfo) {
    String downUrl = QiNiuContent.path + "/" + fileInfo.getPath();

    // 如果需要0个积分或者是文件所属着，则直接返回下载的地址即可
    if (fileInfo.getIntegral() == 0 || Objects.equals(userId, fileInfo.getUserId())) {
      return downUrl;
    }

    // 增加用户的积分
    integralService
        .incrByUserId(fileInfo.getUserId(), fileInfo.getIntegral(), IntegralType.DOWN_FILES.name());

    // 减少下载用户的积分
    integralService.reduceByUserId(userId, fileInfo.getIntegral(), IntegralType.DOWN_FILES.name());

    // 增加下载次数
    this.incrDownNumById(fileInfo.getId());
    return downUrl;
  }


}
