package com.djk.minis.web;

import com.djk.minis.core.ClassPathXmlResource;
import com.djk.minis.core.Resource;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * MVC的核心启动类
 * 用来处理所有的web请求
 */
public class DispatcherServlet extends HttpServlet {
    private List<String> packageNames = new ArrayList<>(); //存放需要扫描的包名
    private Map<String, Object> controllerObjs = new HashMap<>(); //存放controller的名称与对象的映射关系
    private List<String> controllerNames = new ArrayList<>(); //存放controller名称

    private Map<String, Class<?>> ControllerClasses = new HashMap<>(); //存放controller名称与类之间的映射关系
    private Map<String, Object> mappingObjs = new HashMap<>();
    private Map<String, Method> mappingMethods = new HashMap<>();
    private List<String> urlMappingNames = new ArrayList<>();

    public void init(ServletConfig config) throws ServletException {
        //servlet的初始化方法
        super.init(config);

        String configLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            //获取配置文件路径
            xmlPath = this.getServletContext().getResource(configLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //扫描所有的包名
        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        refresh();
    }

    //对扫描到的每一个类进行加载和实例化
    protected void refresh() {
        initController();
        initMapping();
    }

    private void initController() {
        this.controllerNames = scanPackages(packageNames);
        for (String controllerName : controllerNames) {
            try {
                //加载类
                Class<?> clz = Class.forName(controllerName);
                //实例化类
                if (!clz.isInterface()) {
                    Object obj = clz.newInstance();
                    //将类与类名之间的映射关系存入map中
                    this.controllerObjs.put(controllerName, obj);
                    this.ControllerClasses.put(controllerName, clz);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化映射关系
     */
    private void initMapping() {
        for (String controllerName : controllerNames) {
            Class<?> clz = ControllerClasses.get(controllerName);
            Object obj = controllerObjs.get(controllerName);
            if (obj != null) {
                Method[] methods = clz.getDeclaredMethods();
                //遍历方法，看是否有RequestMapping注解
                if (methods != null) {
                    for (Method method : methods) {
                        boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                        if (isRequestMapping) {
                            String urlMapping = method.getAnnotation(RequestMapping.class).value();
                            //建立方法名和URL的映射关系
                            this.urlMappingNames.add(urlMapping);
                            this.mappingObjs.put(urlMapping, obj);
                            this.mappingMethods.put(urlMapping, method);
                        }
                    }
                }

            }
        }
    }

    /**
     * 扫描所有的包
     *
     * @param packageNames
     * @return
     */
    private List<String> scanPackages(List<String> packageNames) {
        List<String> controllerNames = new ArrayList<>();
        for (String packageName : packageNames) {
            controllerNames.addAll(scanPackage(packageName));
        }
        return controllerNames;
    }

    /**
     * 扫描单个包
     *
     * @param packageName
     * @return
     */
    private List<String> scanPackage(String packageName) {
        List<String> controllerNames = new ArrayList<>();
        URI uri = null;
        try {
            //获取包路径
            uri = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //处理对应的文件目录
        File dir = new File(uri);
        for (File f : dir.listFiles()) {
            //对子目录进行递归扫描
            if (f.isDirectory()) {
                scanPackage(packageName + "." + f.getName());
            } else {
                //判断是否为java文件
                if (f.getName().endsWith(".class")) {
                    String controllerName = packageName + "." + f.getName().replace(".class", "");
                    controllerNames.add(controllerName);
                }
            }
        }
        return controllerNames;
    }

    //处理get请求
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求路径
        String sPath = request.getServletPath();

        if (!urlMappingNames.contains(sPath)) {
            response.getWriter().append("404 Not Found");
            return;
        }

        Method method = mappingMethods.get(sPath);
        Object obj = mappingObjs.get(sPath);
        Object objResult = null;
        try {
            objResult = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //将方法返回值写入response
        response.getWriter().append(objResult.toString());
    }

}
