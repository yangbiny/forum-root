package cn.pzhu.forum.biz.files.application;

import cn.pzhu.forum.biz.files.application.cmd.FileCmd;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.FileService;
import cn.pzhu.forum.service.IntegralService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Service
public class FileApplicationService {

  @Resource
  private FileService fileService;

  @Resource
  private IntegralService integralService;

  public boolean addFile(FileCmd cmd) {

    FileInfo fileInfo = convertToFileInfo(cmd);
    return fileService.addFile(fileInfo);
  }

  public List<FileInfo> queryFileInfosByUserId(String userId) {

    if (StringUtils.isEmpty(userId)) {
      return Collections.emptyList();
    }
    return fileService.queryFileInfosByUserId(userId);
  }


  private FileInfo convertToFileInfo(FileCmd cmd) {
    FileInfo fileInfo = new FileInfo();
    fileInfo.setUserId(cmd.getUserId());
    fileInfo.setPath(cmd.getPath());
    fileInfo.setSize(cmd.getSize());
    fileInfo.setTitle(cmd.getTitle());
    fileInfo.setIntroduction(cmd.getIntroduction());
    fileInfo.setIntegral(cmd.getIntegral());
    return fileInfo;

  }

  public boolean deleteFiles(String userId, Integer id) {
    FileInfo fileInfo = fileService.queryFileInfoById(id);

    if (fileInfo == null || !Objects.equals(userId, fileInfo.getUserId())) {
      return false;
    }
    return fileService.deleteFileInfos(id);
  }

  public FileInfo findFileById(Integer fileId) {
    return fileService.queryFileInfoById(fileId);
  }

  public String downFile(String userId, FileInfo fileInfo) {
    return fileService.getDownUrl(userId, fileInfo);
  }

  public List<FileInfo> getFileInfos(Integer start, Integer limit) {
    return fileService.getFileInfo(start, limit);
  }
}
