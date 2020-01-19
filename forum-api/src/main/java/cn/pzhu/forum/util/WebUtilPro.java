package cn.pzhu.forum.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtilPro {

    /**
     * Determine if an asynchronous request
     * @param request Request domain
     * @return True is asynchronous
     */
    public static boolean isAjaxRequest(HttpServletRequest request){

        String requestedWith = request.getHeader("x-requested-with");
        return requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest");

    }

}
