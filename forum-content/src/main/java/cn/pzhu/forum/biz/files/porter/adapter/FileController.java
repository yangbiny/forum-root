package cn.pzhu.forum.biz.files.porter.adapter;

import cn.pzhu.forum.application.exception.IntegralException;
import cn.pzhu.forum.biz.files.application.FileApplicationService;
import cn.pzhu.forum.biz.files.application.cmd.FileCmd;
import cn.pzhu.forum.biz.files.porter.adapter.form.FileForm;
import cn.pzhu.forum.biz.files.porter.adapter.vo.FileInfoVo;
import cn.pzhu.forum.content.QiNiuContent;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.service.IntegralService;
import cn.pzhu.forum.utils.ForumUtils;
import cn.pzhu.forum.utils.Resp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.Region;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Resource;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author impassivey
 */
@Controller
@RequestMapping("files/")
public class FileController {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Resource
  private FileApplicationService fileApplicationService;

  @Resource
  private IntegralService integralService;

  @ResponseBody
  @PostMapping("upload/files/")
  public Map<String, String> uploadToQiniu(
      FileForm fileForm,
      MultipartFile target) throws IOException {

    String userId = SecurityUtils.getSubject().getPrincipal().toString();

    String response = uploadFile(target);

    FileCmd cmd = convertToFileCmd(userId, response, fileForm);

    boolean result = fileApplicationService.addFile(cmd);

    Map<String, String> map = new HashMap<>(2);

    if (result) {
      map.put("msg", "成功");
    } else {
      map.put("msg", "发生错误");
    }

    return map;
  }

  @ResponseBody
  @GetMapping("user/list/")
  public Resp<List<FileInfoVo>> queryFileInfos() {
    String userId = SecurityUtils.getSubject().getPrincipal().toString();
    List<FileInfo> fileInfos = fileApplicationService.queryFileInfosByUserId(userId);
    return new Resp<>(ForumUtils.toList(fileInfos, this::toFileInfoVo));
  }

  @ResponseBody
  @GetMapping("user/down/{fileId}/")
  public Resp<String> downLoadFile(@PathVariable Integer fileId) {
    Object principal = SecurityUtils.getSubject().getPrincipal();
    if(principal == null){
      throw new IntegralException("请登录");
    }
    String userId = principal.toString();
    Integer userIntegral = integralService.findIntegralByUserId(userId);
    FileInfo fileInfo = fileApplicationService.findFileById(fileId);
    if (fileInfo == null) {
      throw new IntegralException("文件信息不存在");
    }
    if (!Objects.equals(fileInfo.getUserId(), userId) && fileInfo.getIntegral() > userIntegral) {
      throw new IntegralException("当前积分不足");
    }
    String downUrl = fileApplicationService.downFile(userId, fileInfo);
    return new Resp<>(downUrl);
  }

  @GetMapping("list/")
  public String fileInfoList(
      @RequestParam(required = false, defaultValue = "0") Integer start,
      @RequestParam(required = false, defaultValue = "10") Integer limit,
      Model model) {

    List<FileInfo> result = fileApplicationService.getFileInfos(start, limit);
    List<FileInfoVo> fileInfoVos = ForumUtils.toList(result, this::toFileInfoVo);
    model.addAttribute("fileInfos", fileInfoVos);
    return "files";
  }

  @ResponseBody
  @DeleteMapping("user/{id}")
  public Resp<Boolean> deleteFileByUser(@PathVariable Integer id) {

    // 获得登录用户的ID
    String userId = SecurityUtils.getSubject().getPrincipal().toString();
    boolean result = fileApplicationService.deleteFiles(userId, id);
    return new Resp<>(result);
  }

  private FileInfoVo toFileInfoVo(FileInfo fileInfo) {
    FileInfoVo fileInfoVo = new FileInfoVo();
    fileInfoVo.setId(fileInfo.getId());
    fileInfoVo.setUserId(fileInfo.getUserId());
    fileInfoVo.setPath(QiNiuContent.path + "/" + fileInfo.getPath());
    fileInfoVo.setTime(fileInfo.getTime());
    fileInfoVo.setSize(fileInfo.getSize());
    fileInfoVo.setTitle(fileInfo.getTitle());
    fileInfoVo.setIntroduction(fileInfo.getIntroduction());
    fileInfoVo.setIntegral(fileInfo.getIntegral());
    fileInfoVo.setDownNum(fileInfo.getDownNum());
    return fileInfoVo;
  }

  private FileCmd convertToFileCmd(String userId, String responseJson, FileForm fileForm) {

    if (StringUtils.isEmpty(responseJson)) {
      throw new InternalException("上传七牛云返回为空");
    }

    FileCmd cmd = new FileCmd();
    cmd.setUserId(userId);

    parseJson(responseJson, cmd);
    cmd.setTitle(fileForm.getTitle());
    cmd.setIntroduction(fileForm.getIntroduction());
    cmd.setIntegral(fileForm.getIntegral());
    return cmd;
  }

  private void parseJson(String responseJson, FileCmd cmd) {
    String[] responses = responseJson.split("\n");

    try {
      String respons = responses[2];

      String replace = respons.replace(",}", "}");

      JsonNode jsonNode = objectMapper.readTree(replace);
      String key = jsonNode.get("key").asText();
      long fsize = jsonNode.get("fsize").asLong();
      cmd.setSize((int) (fsize / 1024));
      cmd.setPath(key);
    } catch (IOException e) {
      throw new IntegralException("解析返回值失败");
    }

  }

  private String uploadFile(MultipartFile target) throws IOException {
    Auth auth = Auth.create(QiNiuContent.accessKey, QiNiuContent.secretKey);
    Configuration configuration = new Configuration(Region.region2());
    UploadManager uploadManager = new UploadManager(configuration);

    StringMap policy = new StringMap();
    policy.putNotEmpty("returnBody",
        "{\"key\":\"$(key)\","
            + "\"hash\":\"$(etag)\","
            + "\"bucket\":\"$(bucket)\","
            + "\"fsize\":\"$(fsize)\","
            + "\"hash\":\"$(etag)\","
            + "\"fname\":\"$(fname)\","
            + "\"mimeType\":\"$(mimeType)\","
            + "\"endUser\":\"$(endUser)\","
            + "\"exif\":\"$(exif)\","
            + "}");

    String fileName = getFileName(target.getOriginalFilename());
    String uploadToken = auth.uploadToken(QiNiuContent.bucket, fileName, 100000, policy);

    Response response = uploadManager
        .put(target.getInputStream(), fileName, uploadToken, policy, null);

    return response == null ? "" : response.getInfo();
  }

  private String getFileName(String originalFilename) {
    StringBuilder stringBuilder = new StringBuilder("files/upload/");
    UUID uuid = UUID.randomUUID();
    String string = uuid.toString();
    stringBuilder.append(string, 0, string.indexOf("-")).append("-").append(originalFilename);
    return stringBuilder.toString();
  }


}
