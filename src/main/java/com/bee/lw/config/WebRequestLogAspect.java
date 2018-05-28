package com.bee.lw.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
  *********************************************************.<br>
  * @description 请求拦截 <br>
  * @author ldw <br>
  * @created 2018/5/28 15:05  <br>
  *********************************************************.<br>
 */
@Aspect
@Component
public class WebRequestLogAspect {
    private static Logger logger = LoggerFactory.getLogger(WebRequestLogAspect.class);

    private  static  long beginTime;

    @Pointcut("execution(public * com.bee.lw.*.*(..))")
    public void webRequestLog() {}

    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            beginTime = System.currentTimeMillis();
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String beanName = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String uri = request.getRequestURI();
            String remoteAddr = getIpAddr(request);
            String method = request.getMethod();
            String params = "";
            if ("POST".equals(method)) {
                Object[] paramsArray = joinPoint.getArgs();
                params = argsArrayToString(paramsArray);
            } else {
                Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                params = paramsMap.toString();
            }
            logger.info(">>>>>>>>>>>>  "+"uri=" + uri + "; beanName=" + beanName + "; remoteAddr=" + remoteAddr  + "; methodName=" + methodName + "; params=" + params+ "; method="+method);
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败doBefore()***", e);
        }
    }

    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result) {
        try {
            Long endTime = System.currentTimeMillis();
            logger.info("<<<<<<<<<<<<  "+ JSONObject.toJSONString(result)+" Time= "+(endTime-beginTime)+"ms");
        } catch (Exception e) {
            logger.error("***操作请求日志记录失败doAfterReturning()***", e);
        }
    }


    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 请求参数拼装
     *
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                Object jsonObj = JSON.toJSON(paramsArray[i]);
                params += jsonObj.toString() + " ";
            }
        }
        return params.trim();
    }

//    ======================================================================================//
//    /***
//     * 获取 request 中 json 字符串的内容
//     *
//     * @param request
//     * @return : <code>byte[]</code>
//     * @throws IOException
//     */
//    public static String getRequestJsonString(HttpServletRequest request)
//            throws IOException {
//        String submitMehtod = request.getMethod();
//        // GET
//        if (submitMehtod.equals("GET")) {
//            return new String(request.getQueryString().getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");
//            // POST
//        } else {
//            return getRequestPostStr(request);
//        }
//    }
//
//    /**
//     * 描述:获取 post 请求的 byte[] 数组
//     * <pre>
//     * 举例：
//     * </pre>
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static byte[] getRequestPostBytes(HttpServletRequest request)
//            throws IOException {
//        int contentLength = request.getContentLength();
//        if(contentLength<0){
//            return null;
//        }
//        byte buffer[] = new byte[contentLength];
//        for (int i = 0; i < contentLength;) {
//
//            int readlen = request.getInputStream().read(buffer, i,
//                    contentLength - i);
//            if (readlen == -1) {
//                break;
//            }
//            i += readlen;
//        }
//        return buffer;
//    }
//
//    /**
//     * 描述:获取 post 请求内容
//     * <pre>
//     * 举例：
//     * </pre>
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static String getRequestPostStr(HttpServletRequest request)
//            throws IOException {
//        byte buffer[] = getRequestPostBytes(request);
//        String charEncoding = request.getCharacterEncoding();
//        if (charEncoding == null) {
//            charEncoding = "UTF-8";
//        }
//        return new String(buffer, charEncoding);
//    }




}
