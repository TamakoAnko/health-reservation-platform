package com.liusp.utils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.net.URLEncoder;
/**
 * 七牛云工具类*/
public class QiniuUtils {
    public static String accessKey = System.getenv("QINIU_ACCESS_KEY");
    public static String secretKey = System.getenv("QINIU_SECRET_KEY");
    public static String bucket = System.getenv("QINIU_BUCKET");//空间名称
    public static void main(String[] args) {

        //上传文件
        QiniuUtils.upload2Qiniu("E:\\03a36073-a140-4942-9b9b-712cecb144901.jpg","abc.jpg");

        //删除文件
        QiniuUtils.deleteFileFromQiniu("abc.jpg");
    }


    public static void upload2Qiniu(String filePath,String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(cfg);Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //上传文件
    public static void upload2Qiniu(byte[] bytes, String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region2());//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);//解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);System.out.println(putRet.key);System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //删除文件
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());System.err.println(ex.response.toString());
        }
    }




}

