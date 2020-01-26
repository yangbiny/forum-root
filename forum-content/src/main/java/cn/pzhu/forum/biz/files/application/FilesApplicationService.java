package cn.pzhu.forum.biz.files.application;

import cn.pzhu.forum.biz.files.application.cmd.FileCmd;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.FileService;
import javax.annotation.Resource;
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
