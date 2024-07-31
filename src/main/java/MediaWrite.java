import io.minio.MinioClient;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 媒体文件与文件信息写入 （写真集输入）
 * <p> 1.文件信息写入数据库 (相关文件信息 作者 写真集)
 * <p> 2.作者表没有查询到作者的条件下 插入作者信息
 * <p> 3.写真集表没有查询到写真集的条件下 插入写真集
 * <p> 4.插入文件信息
 * <p> 5.媒体文件存储到对象存储
 * <p> 6.按序号添加 不做随机数插入 序号起始设定为10001
 * <p>
 * MinIO文件结构
 * 写真桶 -> 作者名-写真集名 [0P0V] -> 文件（图片与视频）
 * photo_lib -> 000000 -> 000.jpg
 * -> 000.mp4
 * 视频桶 -> 车牌号 -> 文件（封面与视频与缩略图）
 * vedio_lib -> SSNI-000 -> cover
 * -> SSNI-000.mp4
 * 影视剧 -> 作品名 -> 相关文件（封面视频缩略图）
 */
public final class MediaWrite {
	// 数据库相关
	private static boolean databaseLinkStatus;
	private static Connection mysqlConnection;
	private static Statement mysqlStatement;
	private static ResultSet resultSet;
	// 对象存储相关
	private static MinioClient minioClient;
	
	// 文件相关
	private static File file;
	
	/**
	 * 创建数据库连接
	 *
	 * @param mysqlInfo 数据库连接信息
	 *                  0:url 1:username 2:password
	 * @param minioInfo 对象存储连接信息
	 *                  0:url 1:ACCESS_KEY 2:SECRET_KEY
	 */
	public static void createMediaLink(String @NotNull [] mysqlInfo, String @NotNull [] minioInfo) {
		// 创建数据库链接
		try {
			// 输入信息 打开Mysql连接
			mysqlConnection = DriverManager.getConnection(mysqlInfo[0], mysqlInfo[1], mysqlInfo[2]);
			mysqlStatement = mysqlConnection.createStatement();
			
			// 初始化客户端
			// 使用密钥与地址建立连接
			minioClient = MinioClient.builder()
					.endpoint(minioInfo[0])
					.credentials(minioInfo[1], minioInfo[2])
					.build();
		} catch (Exception exception) {
			// 初始化失败 抛出错误
			databaseLinkStatus = false;
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
		
		// 连接创建成功
		databaseLinkStatus = true;
	}
	
	/**
	 * 释放资源 关闭数据库连接
	 */
	public static void closeMediaLink() {
		try {
			if (databaseLinkStatus) {
				databaseLinkStatus = false;
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (mysqlStatement != null) {
				mysqlStatement.close();
			}
			if (mysqlConnection != null) {
				mysqlConnection.close();
			}
			if (minioClient != null) {
				minioClient.close();
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
	}
	
	
	/**
	 * 计算SHA-512哈希值
	 *
	 * @param filePath 文件路径
	 * @return 计算状态
	 */
	private static boolean calculateSHA512(String filePath) {
		try {
			// 配置算法
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			// 配置文件输入流
			FileInputStream fis = new FileInputStream(filePath);
			FileChannel channel = fis.getChannel();
			DigestInputStream dis = new DigestInputStream(fis, digest);
			// 创建80KB缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(81920);
			// 计算校验
			while (channel.read(buffer) != -1) {
				// 翻转输入文件输入流
				buffer.flip();
				// 添加文件输入流
				digest.update(buffer);
				// 清空buffer
				buffer.clear();
			}
			// 释放资源
			dis.close();
			channel.close();
			fis.close();
			// 正常返回哈希值
			return true;
		} catch (NoSuchAlgorithmException | IOException exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
	}
	
	/**
	 * 上传文件
	 * 1.写入文件信息到数据库
	 * 2.上传文件到对象存储
	 *
	 * @param fileInfo 文件信息
	 *                 0:文件地址 1:上传目标路径 2:作者ID 3:写真集ID 4:……
	 */
	public static void uploadFile(String[] fileInfo) {
		if (databaseLinkStatus) {
			return;
		}
		// 插入数据
		
		
		// 上传文件
		file = new File(fileInfo[0]);
	}
	
	// 测试主函数代码
	public static void main(String[] args) {
		String[] mysqlInfo = {"jdbc:mysql://localhost:3306/", "root", "Zxcvbnm123++"};
		String[] minioInfo = {"http://192.168.1.21:9000", "k7rAIrfc42MjtBLuAUQX", "uZcEXKKhDExQkXCRGdDs7x79G2TOLzqLL6hcxaM1"};
		// 创建数据库连接
		createMediaLink(mysqlInfo, minioInfo);
		// 释放资源
		closeMediaLink();
	}
}
