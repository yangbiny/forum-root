package cn.pzhu.forum.biz.files.application;

import cn.pzhu.forum.biz.files.application.cmd.FileCmd;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.FileService;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author impassivey
 */
@Service
public class FilesApplicationService {

  @Resource
  private FileService fileService;

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
}
