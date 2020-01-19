package cn.pzhu.forum.realm;

import cn.pzhu.forum.content.URLContent;
import cn.pzhu.forum.util.ResultData;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShiroLoginFilter extends FormAuthenticationFilter {

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     *
     * @param request  请求域
     * @param response 响应域
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws IOException IO异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (isAjax(request)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            ResultData resultData = new ResultData();
            resultData.setCode(403);
            resultData.setMessage("登录认证失效，请重新登录!");
            httpServletResponse.getWriter().write(JSONObject.toJSON(resultData).toString());
        } else {
            /*
             * @Mark 非ajax请求重定向为登录页面
             */

            httpServletResponse.sendRedirect(URLContent.LOGIN);
        }
        return false;
    }

    private boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}

