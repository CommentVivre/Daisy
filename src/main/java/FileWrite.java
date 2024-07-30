// minIO接口读写类
// 用于向minIO写入数据

/*
 * MinIO文件结构
 * 写真桶 -> 作者名-写真集名 [0P0V] -> 文件（图片与视频）
 * photo_lib -> 000000 -> 000.jpg
 *                     -> 000.mp4
 * 视频桶 -> 车牌号 -> 文件（封面与视频与缩略图）
 * vedio_lib -> SSNI-000 -> cover
 *                       -> SSNI-000.mp4
 * 影视剧 -> 作品名 -> 相关文件（封面视频缩略图）
 */
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;

import java.io.*;

public final class FileWrite {
    private static final String url="http://192.168.1.21:9000";
    private static final String ACCESS_KEY="k7rAIrfc42MjtBLuAUQX";
    private static final String SECRET_KEY="uZcEXKKhDExQkXCRGdDs7x79G2TOLzqLL6hcxaM1";

    public static void minioTest(){
        try{
            // 初始化客户端
            // 使用密钥与地址建立连接
            MinioClient minioClient = MinioClient.builder()
                        .endpoint(url)
                        .credentials(ACCESS_KEY, SECRET_KEY)
                        .build();

            // 1. 创建InputStream上传
            File file = new File("D:\\TestPhoto\\Hikari Yuka[ROGLE] Vol.1 [63P]\\0002.jpg");
            InputStream fileInputStream = new FileInputStream(file);
            // 上传
            minioClient.putObject(PutObjectArgs.builder()
                        .bucket("tesbucket")
                        .object("00001/"+file.getName())
                        .stream(fileInputStream, fileInputStream.available(), -1)
                        .build());

            // 指定一个GET请求，返回获取文件对象的URL，此URL过期时间为一天
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket("tesbucket")
                        .object("00001/0002.jpg")
                        .expiry(60 * 60 * 24)
                        .build());
            // 输出url
            System.out.println(url);
            // 释放资源
            fileInputStream.close();
            minioClient.close();
        }
        catch(MinioException exception){
            System.out.println(exception.getMessage());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public static void mysqlTest(){

    }
}
