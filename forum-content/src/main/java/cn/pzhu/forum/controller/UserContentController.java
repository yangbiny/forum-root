package cn.pzhu.forum.controller;

import cn.pzhu.forum.biz.files.application.FilesApplicationService;
import cn.pzhu.forum.biz.files.porter.adapter.vo.FileInfoVo;
import cn.pzhu.forum.content.QiNiuContent;
import cn.pzhu.forum.content.URLContent;
import cn.pzhu.forum.entity.Article;
import cn.pzhu.forum.entity.FileInfo;
import cn.pzhu.forum.entity.Reply;
import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.service.ArticleService;
import cn.pzhu.forum.service.IntegralService;
import cn.pzhu.forum.service.ReplyService;
import cn.pzhu.forum.service.SortService;
import cn.pzhu.forum.service.UserInfoService;
import cn.pzhu.forum.util.ResultData;
import cn.pzhu.forum.util.Utils;
import cn.pzhu.forum.utils.ForumUtils;
import com.google.zxing.WriterException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: forum-root
 * @description: 用户访问论坛系统
 * @author: Impassive
 * @create: 2019-06-08 09:00
 **/
@Controller
@Slf4j
public class UserContentController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SortService sortService;

    @Autowired
    private ReplyService replyService;

    @Resource
    private FilesApplicationService filesApplicationService;

    @Resource
    private IntegralService integralService;

    /**
     * 编写博客前查询基本信息
     *
     * @param session 用于存储数据
     * @return 编写博客页面信息
     */
    @RequestMapping("/user/article/editor")
    public String editor(HttpSession session) {

        log.info("cn.pzhu.forum.controller.ArticleController.editor-编辑博客前的准备");

        List<Sort> list = sortService.list();

        session.setAttribute("sortList", list);

        return "articleEditor";
    }

    /**
     * 添加博客
     *
     * @param context 博客内容（HTML格式）
     * @param title   博客标题
     * @param body    博客内容（MarkDown格式）
     * @param sort    博客所属类别
     * @param model   数据域
     * @return 博客添加成功页面信息
     */
    @RequestMapping("/user/article/editor/add")
    public String add(String context, String title, String body, int sort, Model model) {

        log.info("cn.pzhu.forum.controller.ArticleController.add-添加博客-入参：" +
            "title = {},sort = {}", title, sort);

        // 获得用户信息
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        if (userId == null) {
            throw new RuntimeException("未登录");
        }
        UserInfo userInfo = userInfoService.get(userId);

        // 封装博客信息
        Article article = new Article();
        article.setContext(context); // HTML格式
        article.setContextMd(body);  // MarkDown格式
        article.setTitle(title);
        article.setSortId(sort);
        article.setUserName(userInfo.getNickName());
        article.setUserId(userId);
        article.setReadNumber(0);
        article.setTop(0);

        // 添加博客
        String principal = articleService.add(article);

        model.addAttribute("user", userInfoService.get(userId).getNickName());
        model.addAttribute("articlePrincipal", principal);
        model.addAttribute("title", title);
        model.addAttribute("userName", userInfo.getNickName());
        model.addAttribute("avatar", userInfo.getAvatar());

        return "success";
    }

    /**
     * 更新用户信息
     *
     * @param response 用于重定向页面
     */
    @RequestMapping("/user/updateInfo")
    public void forward(HttpServletResponse response) {

        try {
            response.sendRedirect(URLContent.UPDATEUSERINFO);
            //request.getRequestDispatcher("http://localhost:8001/user/updateInfo").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //response.sendRedirect("http://localhost:8001/user/updateInfo");

    }

    /**
     * 查询用户已发表的博客
     *
     * @param model 数据域
     * @return 用户博客信息页面
     */
    @RequestMapping("/user/article/list")
    public String selfList(Model model) {

        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        UserInfo userInfo = userInfoService.get(principal);

        Integer integral = integralService.findIntegralByUserId(principal);
        userInfo.setIntegral(integral);
        model.addAttribute("userInfo", userInfo);

        List<Article> articles = articleService.userList(userInfo.getNickName());
        model.addAttribute("articles", articles);

        List<FileInfo> fileInfos = filesApplicationService.queryFileInfosByUserId(principal);
        model.addAttribute("fileInfos", ForumUtils.toList(fileInfos, this::toFileInfoVo));

        return "blog";
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

    /**
     * editormd中上传图片文件
     *
     * @param file 图片文件信息
     * @return 状态结果集
     */
    @RequestMapping("/user/article/upload")
    @ResponseBody
    public Map<String, Object> upload(
        @RequestParam(value = "editormd-image-file", required = false) MultipartFile file) {

        log.info("cn.pzhu.forum.controller.ArticleController.upload-editormd上传图片");

        Map<String, Object> map = new HashMap<>();

        try {

            String originalFilename = file.getOriginalFilename();

            ByteArrayInputStream inputStream = (ByteArrayInputStream) file.getInputStream();

            String path = Utils.uploadImg(inputStream, Utils.getRandomName() + "-" + originalFilename);

            if (path.equals("")) {
                log.error("错误!");
                map.put("success", 0);
                map.put("message", "错误!");
            } else {
                map.put("success", 1);
                map.put("message", "成功!");
                map.put("url", path);
            }

        } catch (IOException e) {

            log.error("cn.pzhu.forum.controller.ArticleController#upload-七牛云上传文件-发生异常-{}",
                    Arrays.toString(e.getStackTrace()));

            e.printStackTrace();
        }

        return map;
    }

    /**
     * 博客分享二维码信息
     *
     * @param url 博客的链接地址
     * @return 二维码信息体（状态码、头部信息、响应体内容）
     */
    @RequestMapping("/user/article/share/image")
    public ResponseEntity<byte[]> image(String url) {

        log.info("cn.pzhu.forum.controller.ArticleController.image-生成博客分享二维码-入参：" +
                "博客链接 URL = {}", url);

        try {
            byte[] qrCode = Utils.getQRCode(url, 300, 300);

            HttpHeaders httpHeaders = new HttpHeaders();

            httpHeaders.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(qrCode, httpHeaders, HttpStatus.CREATED);

        } catch (WriterException | IOException e) {
            log.error("cn.pzhu.forum.controller.ArticleController.image-生成博客分享二维码-发生异常- " +
                    "{} ", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 用户发表新的留言
     *
     * @param reply 留言内容
     * @return 操作的结果集
     */
    @RequestMapping("/user/article/reply")
    @ResponseBody
    public ResultData reply(Reply reply) {

        ResultData resultData = new ResultData();
        String principal = (String) SecurityUtils.getSubject().getPrincipal();

        UserInfo userInfo = userInfoService.get(principal);

        reply.setContent(contentSplit(reply.getContent()));
        reply.setUserName(userInfo.getNickName());
        reply.setAvatar(userInfo.getAvatar());
        reply.setUserId(userInfo.getId());

        boolean add = replyService.add(reply);

        if (add) {
            resultData.setMessage("成功!");
        } else {
            resultData.setMessage("失败!");
        }

        return resultData;
    }

    /**
     * 删除博客
     *
     * @param id 博客ID
     * @return 操作结果
     */
    @RequestMapping("/user/article/delete")
    @ResponseBody
    public Map<String, String> delete(int id) {

        log.info("cn.pzhu.forum.controller.ArticleController.delete-删除指定的博客-入参：id = {}", id);

        Map<String, String> map = new HashMap<>();

        boolean delete = articleService.delete(id);

        if (delete) {
            map.put("msg", "成功");
        } else {
            map.put("msg", "失败");
        }

        return map;
    }

    /**
     * 切割回复体的字符创。直接留言的时候就直接返回，回复别人留言信息需要将回复提示信息去除
     *
     * @param content 回复体
     * @return 切割后的留言内容
     */
    private String contentSplit(String content) {

        if (content.startsWith("[--Reply To ")) {

            int lastIndex = content.lastIndexOf(" --]");

            return content.substring(lastIndex + 5);

        }

        return content;
    }

    /**
     * 更新博客
     *
     * @param id        博客ID
     * @param context   博客内容（HTML格式）
     * @param title     博客标题
     * @param body      博客内容（MarkDown格式）
     * @param sort      博客所属类别
     * @param principal 博客的主要关键字
     * @param model     数据域
     * @return 博客添加成功页面信息
     */
    @RequestMapping("/user/article/editor/update")
    public String update(Integer id, String context, String title, String body, int sort, String principal,
                         Model model) {
        log.info("cn.pzhu.forum.controller.ArticleController.add-添加博客-入参：" +
                "title = {},sort = {}", title, sort);

        // 获得用户信息
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.get(userId);

        // 封装博客信息
        Article article = new Article();
        article.setId(id);
        article.setContext(context); // HTML格式
        article.setContextMd(body);  // MarkDown格式
        article.setTitle(title);
        article.setSortId(sort);

        // 修改
        boolean update = articleService.update(article);

        model.addAttribute("user", userInfoService.get(userId).getNickName());
        model.addAttribute("articlePrincipal", principal);
        model.addAttribute("title", title);
        model.addAttribute("userName", userInfo.getNickName());
        model.addAttribute("avatar", userInfo.getAvatar());

        return "success";
    }

    /**
     * 更新博客信息前查询对应的博客信息
     *
     * @param id      博客的ID
     * @param model   数据域
     * @param session 存数数据
     * @return 博客更新页面
     */
    @RequestMapping("/user/article/editor/get")
    public String updateGet(int id, Model model, HttpSession session) {

        @SuppressWarnings("unchecked")
        List<Sort> sortList = (List<Sort>) session.getAttribute("sortList");

        if (sortList == null || sortList.size() == 0) {
            List<Sort> list = sortService.list();

            session.setAttribute("sortList", list);
        }
        Article article = articleService.get(id);
        model.addAttribute("article", article);
        return "articleUpdate";
    }

    @RequestMapping("/user/article/like")
    @ResponseBody
    public Map<String, String> like(String principal) {

        Map<String, String> map = new HashMap<>();

        String principal1 = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo userInfo = userInfoService.get(principal1);

        boolean flag = articleService.hasLiked(userInfo.getNickName(), principal);

        if (flag) {
            map.put("msg", "已经点赞");

            return map;
        }

        Integer like = articleService.like(userInfo.getNickName(), principal);

        if (like > -1) {
            map.put("msg", "成功");
        } else {
            map.put("msg", "失败");
        }

        return map;
    }

}
