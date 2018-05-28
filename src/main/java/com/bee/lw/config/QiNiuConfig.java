package com.bee.lw.config;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * ********************************************************.<br>
 *
 * @author ldw <br>
 * @classname QiNiuConfig <br>
 * @description TODO <br>
 * @created 2018/5/23 10:44 <br>
 * ********************************************************.<br>
 */
//@Component
public class QiNiuConfig {
//    Zone zone = Zone.autoZone();
//    Configuration config = new Configuration(zone);
//    //...其他参数参考类注释
//    UploadManager uploadManager = new UploadManager(cfg);
//    //...生成上传凭证，然后准备上传
//    String accessKey = "your access key";
//    String secretKey = "your secret key";
//    String bucket = "your bucket name";
//    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//    String localFilePath = "/home/qiniu/test.png";
//    //默认不指定key的情况下，以文件内容的hash值作为文件名
//    String key = null;
//    Auth auth = Auth.create(accessKey, secretKey);
//    String upToken = auth.uploadToken(bucket);
//    try{
//        Response response = uploadManager.put(localFilePath, key, upToken);
//        //解析上传成功的结果
//        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//        System.out.println(putRet.key);
//        System.out.println(putRet.hash);
//    } catch(QiniuException ex){
//        Response r = ex.response;
//        System.err.println(r.toString());
//        try {
//            System.err.println(r.bodyString());
//        } catch (QiniuException ex2) {
//            //ignore
//        }
//    }

    //获取授权对象
    Auth auth = Auth.create("*", "*");

    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
    // Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone zone = Zone.autoZone();

    Configuration config = new Configuration(zone);
    UploadManager uploadManager = new UploadManager(config);

    /**
     * 获取凭证
     *
     * @param bucketName 空间名称
     * @return
     */
    public String getUpToken(String bucketName, String key) {
        //insertOnly 如果希望只能上传指定key的文件，并且不允许修改，那么可以将下面的 insertOnly 属性值设为 1
        return auth.uploadToken(bucketName, key, 3600, new StringMap().put("insertOnly", 0));
    }

    /**
     * 覆盖上传
     *
     * @param path       上传文件路径
     * @param bucketName 空间名
     * @param key        文件名
     */
    public void overrideUpload(String path, String bucketName, String key) {
        try {
            String token = getUpToken(bucketName, key);//获取 token
            Response response = uploadManager.put(path, key, token);//执行上传，通过token来识别 该上传是“覆盖上传”
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+putRet.key);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+putRet.hash);
        } catch (QiniuException e) {
            System.out.println(e.response.statusCode);
            e.printStackTrace();
        }
    }

    /**
     * 主函数：测试
     *
     * @param args
     */
    public static void main(String[] args) {
        // 上传文件的路径，因为在Mac下，所以路径和windows下不同
        String filePath = "C:\\Users\\liao\\Desktop\\1527489173(1).jpg";
        // 要上传的空间
        String bucketName = "*";
        // 上传到七牛后保存的文件名
        String key = "15274858734.jpg";

        new QiNiuConfig().overrideUpload(filePath, bucketName, key);
    }
}
