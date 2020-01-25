package cn.pzhu.forum.controller;

import cn.pzhu.forum.application.LoginApplicationService;
import cn.pzhu.forum.entity.Major;
import cn.pzhu.forum.entity.School;
import cn.pzhu.forum.entity.User;
import cn.pzhu.forum.entity.UserInfo;
import cn.pzhu.forum.realm.EasyTypeToken;
import cn.pzhu.forum.service.MajorService;
import cn.pzhu.forum.service.SchoolService;
import cn.pzhu.forum.service.UserInfoService;
import cn.pzhu.forum.service.UserService;
import cn.pzhu.forum.util.Utils;
import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class UserLogin {

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private SchoolService schoolService;

    @Resource
    private MajorService majorService;

    @Resource
    private LoginApplicationService loginApplicationService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 用户注销登录信息
     *
     * @return 返回用户登录页面
     */
    @RequestMapping("logout")
    public String logout() {
        log.info("用户注销登录信息");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }

    /**
     * 更新用户头像
     *
     * @param multipartFile 头像文件
     * @return 更新成功返回头像路径
     */
    @ResponseBody
    @RequestMapping("/user/uploadAvatar")
    public String uploadAvatar(MultipartFile multipartFile) {

        String id = SecurityUtils.getSubject().getPrincipal().toString();

        String path = id + multipartFile.getOriginalFilename();

        String s = userService.updateUserAvatar(id, multipartFile, path);

        if (!"".equals(s)) {
            return s;
        }
        return "error";
    }

    /**
     * QQ登录
     *
     * @param request  请求域
     * @param response 响应域
     * @throws IOException IO异常
     */
    @RequestMapping("qqLogin")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("cn.pzhu.forum.controller.UserLogin.qqLogin-用户使用QQ登录");

        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));//将页面重定向到qq第三方的登录页面
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户信息前查询用户的信息
     *
     * @param model 存储数据
     * @return 返回更新页面
     */
    @RequestMapping("/user/updateInfo")
    public String updateInfo(Model model) {

        log.info("cn.pzhu.forum.controller.UserLogin.updateInfo-更新用户信息前查询用户现有信息");

        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();

        if (principal == null) {
            logout();
        }

        assert principal != null;
        UserInfo userInfo = userInfoService.get(principal.toString());

        model.addAttribute("userInfo", userInfo);
        log.info("待更新用户的信息-useInfo = {}", userInfo.toString());

        List<School> schools = schoolService.list();
        model.addAttribute("schools", schools);

        List<Major> majors = majorService.list(schools.get(0).getId());
        model.addAttribute("majors", majors);

        return "setting";
    }

    /**
     * 查询指定school的专业信息
     *
     * @param id School Id
     * @return 专业信息
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<Major> list(int id) {
        log.info("根据学院ID查询学院下的专业");

        List<Major> list = majorService.list(id);

        log.info("查询结果数：{}", list == null ? 0 : list.size());

        return list;
    }

    /**
     * 更新所有的用户信息
     *
     * @param userInfo 需要更新的用户信息
     * @return 更新结果
     */
    @RequestMapping(value = "/user/updateInfos")
    @ResponseBody
    public Map<String, String> updateInfo(UserInfo userInfo) {

        log.info("cn.pzhu.forum.controller.UserLogin.updateInfo(cn.pzhu.forum.entity.UserInfo-更新用户信息-入参：" +
                "userInfo = {}", userInfo.toString());

        Map<String, String> map = new HashMap<>();

        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        assert principal != null;
        userInfo.setId(principal.toString());

        boolean b = userService.updateUserInfo(userInfo);

        log.info("controller控制层接收到的返回值: flag = {}", b);

        map.put("msg", "成功");

        return map;
    }

    /**
     * 用户登录
     *
     * @param user 登录用户信息
     * @param code 图片验证码信息
     * @return 登录状态及用户身份
     */
    @RequestMapping("login")
    @ResponseBody
    public Map<String, String> userLogin(User user, String code) {

        log.info("cn.pzhu.forum.controller.UserLogin.userLogin-用户登录-入参：" +
            "user = {},code = {}", user.toString(), code);

        Map<String, String> map = new HashMap<>();
        /*Object rCode = session.getAttribute("RCode");*/

        /*
        if (code.toLowerCase().equals(rCode.toString().toLowerCase())) {
            map.put("msg", "验证码错误!");
            return map;
        }
        */

        Subject subject = SecurityUtils.getSubject();
        EasyTypeToken token = new EasyTypeToken(user.getId(), user.getPassword());
        // 用户已经成功登录
        if (!subject.isAuthenticated()) {
            try {
                subject.login(token);

                loginApplicationService.whenUserLoginSuccess(user.getId());
                // 登录成功后判断是否有管理员权限
                boolean admin = subject.hasRole("admin");

                if (admin) {
                    // 管理员去到管理员界面
                    map.put("msg", "admin");
                } else {
                    // 用户去到用户界面
                    map.put("msg", "user");
                }

                return map;
            } catch (AuthenticationException e) {
                e.printStackTrace();
                map.put("msg", "账号或密码错误");
            }
        } else {
            // 登录成功后判断是否有管理员权限
            boolean admin = subject.hasRole("admin");

            if (admin) {
                // 管理员去到管理员界面
                map.put("msg", "admin");
            } else {
                // 用户去到用户界面
                map.put("msg", "user");
            }
        }

        return map;
    }

    /**
     * 注册
     *
     * @param user     注册用户信息
     * @param userCode 邮件验证码
     * @param session  存数数据
     * @return 注册结果
     */
    @RequestMapping("register")
    @ResponseBody
    public String register(User user, @RequestParam("username") String username,
                           @RequestParam("userCode") String userCode, HttpSession session) {
        // 获得生成的邮件验证码
        Object code = session.getAttribute("code");

        // 检查用户名是否被使用
        Map<String, Boolean> map = checkUserName(username);

        // flag 为检查信息的标志
        if (map.get("flag")) {
            return "error";
        }

        if (code.equals(userCode)) {
            session.setAttribute("code", null);
            boolean b = userService.addUser(user, username);
            if (b) {
                return "success";
            } else {
                return "error";
            }
        }
        return "error";
    }

    /**
     * 检查用户名是否已经存在
     *
     * @param username 用户名
     * @return 检查结果集合 flag为true表示用户名已经被使用
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public Map<String, Boolean> checkUserName(String username) {
        Map<String, Boolean> map = new HashMap<>();

        boolean flag = userService.checkUserName(username);

        if (!flag) {
            map.put("flag", true);
        } else {
            map.put("flag", false);
        }

        return map;
    }

    /**
     * 密码重置
     *
     * @param user     用户信息
     * @param userCode 邮件验证码
     * @param code     图片验证码
     * @param session  存数数据
     * @return 重置结果及状态
     */
    @RequestMapping("resetPassword")
    @ResponseBody
    public Map<String, String> resetPassword(User user, @RequestParam("userCode") String userCode,
                                             @RequestParam("code") String code, HttpSession session) {

        log.info("cn.pzhu.forum.controller.UserLogin.resetPassword-用户重置密码-入参：" +
                "user = {},userCode = {},code = {}", user.toString(), userCode, code);

        Map<String, String> map = new HashMap<>();

        String rUserCode = (String) session.getAttribute("code");  // 验证邮件
        Object rCode = session.getAttribute("RCode");              // 验证图片验证码

        try {
            if (!(userCode.toLowerCase().equals(rUserCode.toLowerCase()) /*&&
                    rCode.toString().toLowerCase().equals(code.toLowerCase())*/)) {
                map.put("msg", "验证码错误");
                return map;
            }
        } catch (NullPointerException e) {   //用户未发送邮件验证码时出现空指针异常
            log.info("邮件发送异常");
            map.put("msg", "验证码错误");
            return map;
        }

        boolean b = userService.resetPassword(user);

        if (b) {
            map.put("msg", "密码重置成功!");
            map.put("flag", "success");
        } else {
            map.put("msg", "密码重置失败!");
        }

        return map;
    }

    /**
     * 生成邮件验证码
     *
     * @param email   需要发送邮件的邮箱
     * @param session 存储数据
     * @return 发送状态
     */
    @RequestMapping("generateRegisterCode")
    @ResponseBody
    public String generateRegisterCode(String email, HttpSession session) {

        String s = Utils.generatedCode(6);  // 生成六位数的数字验证码
        session.setAttribute("code", s);  // 保存验证码，注册或者重置密码时使用

        log.info("cn.pzhu.forum.controller.UserLogin.generateRegisterCode-生成邮件验证码-code = {}", s);

        boolean type = Utils.sendMail(s, email, "注册验证码");  // 发送邮件  发送类型为注册验证码

        if (type) {
            return "success";
        }
        return "error";
    }

    /**
     * 生成图片验证码
     *
     * @param request  请求域
     * @param response 响应域
     */
    @RequestMapping("/**/GenerateVerificationCode")
    @ResponseBody
    public void GenerateVerificationCode(HttpServletRequest request, HttpServletResponse response) {

        log.info("cn.pzhu.forum.controller.UserLogin.GenerateVerificationCode-生成图片验证码");

        /* 禁止缓存 */
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);

        //指定生成的响应图片,一定不能缺少这句话,否则错误.
        response.setContentType("image/jpeg");

        int width = 130;
        int height = 40;

        Map<String, BufferedImage> map = Utils.GenerateVerificationCode(width, height);

        if (!map.isEmpty()) {
            HttpSession session = request.getSession(true);

            Set<String> strings = map.keySet();

            for (String rCode : strings) {
                session.setAttribute("RCode", rCode);

                BufferedImage image = map.get(rCode);
                try {

                    ImageIO.write(image, "JPEG", response.getOutputStream());

                } catch (IOException e) {
                    log.info("生成图片验证码错误");
                    e.printStackTrace();
                }
            }

        }

    }

}
