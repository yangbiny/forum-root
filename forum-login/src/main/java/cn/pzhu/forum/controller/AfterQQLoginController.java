package cn.pzhu.forum.controller;

import cn.pzhu.forum.content.URLContent;
import cn.pzhu.forum.entity.User;
import cn.pzhu.forum.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Controller
public class AfterQQLoginController {

    @Autowired
    private UserService userService;

    /**
     * QQ登录成功后，会根据配置文件中的redirect_URI执行该函数
     *
     * @param code     用户回调的唯一编码
     * @param request  请求域
     * @param response 响应请求
     * @param model    数据存储
     * @return 登录成功跳转页面
     */
    @RequestMapping("/afterLogin")
    public String afterLogin(String code, HttpServletRequest request, HttpServletResponse response, Model model) {

        //填写Appid,appkey和回调地址
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&" +
                "client_id=101567422&client_secret=053d0a5fe49a77941bd80d896a2947c9" +
                "&redirect_uri=http://www.localhost:8001/afterLogin&code=" + code;
        String responseStr = httpRequest(url);

        //获取access_Token
        String[] tokens = responseStr.split("&");
        String token = tokens[0];
        String userUrl = "https://graph.qq.com/oauth2.0/me?" + token;
        String sr = httpRequest(userUrl);
        JSONObject result = JSON.parseObject(sr.substring(10, sr.length() - 3));
        //获取Open_ID,每一个用户的openID是唯一的
        String openId = (String) result.get("openid");

        User user = userService.get(openId);

        if (user == null) {

            model.addAttribute("openId", openId);
            return "userInfo";

        } else {

            PrincipalCollection principals = new SimplePrincipalCollection(user.getId(), "ShiroRealm");

            Subject.Builder builder = new WebSubject.Builder(request, response);
            builder.principals(principals);
            builder.authenticated(true);
            builder.sessionId(request.getSession().getId());
            Subject subject = builder.buildSubject();
            ThreadContext.bind(subject);

            boolean admin = subject.hasRole("admin");
            try {
                if (admin) {

                    response.sendRedirect(URLContent.ADMIN);
                    return null;

                } else {

                    response.sendRedirect(URLContent.CONTENT);
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    /**
     * 链接指定的url,并获得链接URL的返回内容
     *
     * @param url 指定的URL
     * @return URL的返回内容
     */
    private String httpRequest(String url) {
        HttpURLConnection urlConnection = null;
        String responseStr = "";
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("Accept-Charset", "utf-8");
            urlConnection.setRequestProperty("contentType", "utf-8");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();

            byte[] bytes;
            bytes = new byte[inputStream.available()];
            int read = inputStream.read(bytes);
            responseStr = new String(bytes);

            // responseStr = StreamToString.ConvertToString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }


}
