package cn.pzhu.forum.biz.files.porter.adapter;

import cn.pzhu.forum.application.exception.IntegralException;
import cn.pzhu.forum.biz.files.application.FilesApplicationService;
import cn.pzhu.forum.biz.files.application.cmd.FileCmd;
import cn.pzhu.forum.biz.files.porter.adapter.form.FileForm;
import cn.pzhu.forum.content.QiNiuContent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.Region;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.Resource;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author impassivey
 */
@RestController
@RequestMapping("files/")
public class FileController {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Resource
  private FilesApplicationService filesApplicationService;

  @PostMapping("upload/files/")
  public String uploadToQiniu(
      FileForm fileForm,
      MultipartFile target) throws IOException {

    //String userId = SecurityUtils.getSubject().getPrincipal().toString();
    String userId = "1963559122@qq.com";

    String response = uploadFile(target);

    FileCmd cmd = convertToFileCmd(userId, response, fileForm);

    boolean result = filesApplicationService.addFile(cmd);

    return null;
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
    System.out.println(responseJson);
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
