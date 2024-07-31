import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Integer.toHexString;


/**
 * Cyclic_redundancy_check
 * 循环冗余校验算法
 * 方法输入byte数组 返回对应校验码
 */
final class Cyclic_redundancy_check {
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC8(byte[] buffer) {
		int wCRCin = 0x00;
		int wCPoly = 0x07;
		for (byte b : buffer) {
			for (int j = 0; j < 8; j++) {
				boolean bit = ((b >> (7 - j) & 1) == 1);
				boolean c07 = ((wCRCin >> 7 & 1) == 1);
				wCRCin <<= 1;
				if (c07 ^ bit)
					wCRCin ^= wCPoly;
			}
		}
		wCRCin &= 0xFF;
		return wCRCin ^= 0x00;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC8_DARC(byte[] buffer) {
		int wCRCin = 0x00;
		int wCPoly = 0x9C;
		for (byte b : buffer) {
			wCRCin ^= (b & 0xFF);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x01) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0x00;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC8_ITU(byte[] buffer) {
		int wCRCin = 0x00;
		int wCPoly = 0x07;
		for (byte b : buffer) {
			for (int j = 0; j < 8; j++) {
				boolean bit = ((b >> (7 - j) & 1) == 1);
				boolean c07 = ((wCRCin >> 7 & 1) == 1);
				wCRCin <<= 1;
				if (c07 ^ bit)
					wCRCin ^= wCPoly;
			}
		}
		wCRCin &= 0xFF;
		return wCRCin ^= 0x55;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC8_MAXIM(byte[] buffer) {
		int wCRCin = 0x00;
		int wCPoly = 0x8C;
		for (byte b : buffer) {
			wCRCin ^= (b & 0xFF);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x01) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0x00;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC8_ROHC(byte[] buffer) {
		int wCRCin = 0xFF;
		// Integer.reverse(0x07) >>> 24
		int wCPoly = 0xE0;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0xFF);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x01) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0x00;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_CCITT(byte[] buffer) {
		int wCRCin = 0x0000;
		int wCPoly = 0x8408;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0x0000;
		
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_CCITT_FALSE(byte[] buffer) {
		int wCRCin = 0xffff;
		int wCPoly = 0x1021;
		for (byte b : buffer) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((wCRCin >> 15 & 1) == 1);
				wCRCin <<= 1;
				if (c15 ^ bit)
					wCRCin ^= wCPoly;
			}
		}
		wCRCin &= 0xffff;
		return wCRCin ^= 0x0000;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_XMODEM(byte[] buffer) {
		int wCRCin = 0x0000;
		int wCPoly = 0x1021;
		for (byte b : buffer) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((wCRCin >> 15 & 1) == 1);
				wCRCin <<= 1;
				if (c15 ^ bit)
					wCRCin ^= wCPoly;
			}
		}
		wCRCin &= 0xffff;
		return wCRCin ^= 0x0000;
	}
	
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_X25(byte[] buffer) {
		int wCRCin = 0xffff;
		int wCPoly = 0x8408;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0xffff;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_MODBUS(byte[] buffer) {
		int wCRCin = 0xffff;
		int POLYNOMIAL = 0xa001;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= POLYNOMIAL;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0x0000;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_IBM(byte[] buffer) {
		int wCRCin = 0x0000;
		int wCPoly = 0xa001;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0x0000;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_MAXIM(byte[] buffer) {
		int wCRCin = 0x0000;
		int wCPoly = 0xa001;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0xffff;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_USB(byte[] buffer) {
		int wCRCin = 0xFFFF;
		int wCPoly = 0xa001;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0xffff;
	}
	
	/**
	 * @param buffer 待计算校验数组
	 * @return 计算结果
	 */
	public static int CRC16_DNP(byte[] buffer) {
		int wCRCin = 0x0000;
		int wCPoly = 0xA6BC;
		for (byte b : buffer) {
			wCRCin ^= ((int) b & 0x00ff);
			for (int j = 0; j < 8; j++) {
				if ((wCRCin & 0x0001) != 0) {
					wCRCin >>= 1;
					wCRCin ^= wCPoly;
				} else {
					wCRCin >>= 1;
				}
			}
		}
		return wCRCin ^= 0xffff;
	}
}

/**
 * 页面爬取参考代码
 */
final class JobProcessor implements PageProcessor {
	//负责解析页面
	@Override
	public void process(Page page) {
		// 解析Page，返回的数据Page，并且把解析的结果放到
		page.putField("divTop", page.getHtml().css("div.sidebar-box div").all());
		// XPath
		// page.putField("div",page.getHtml().xpath("//div[@id=main-left-cloumn]/div"));
		// CSS选择器
		// page.putField("divCSS",page.getHtml().css("div > a.item-top > strong").toString());
	}
	
	private Site site = Site.me();
	
	@Override
	public Site getSite() {
		return site;
	}
	
	//主函数
	public static void main(String[] args) {
		// HttpClient爬取页面
		try {
			//1、打开浏览器，创建HttpClient对象
			CloseableHttpResponse response;
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				//2、输入网址,创建发起get请求HttpGet对象
				HttpGet httpGet = new HttpGet("https://www.runoob.com/");
				//3、按回车，发起请求，返回响应，使用HttpClient对象发起请求
				response = httpClient.execute(httpGet);
			}
			//4、解析响应，响应数据
			//判断状态码是否是200
			if (response.getCode() == 200) {
				System.out.println("响应成功");
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity, "UTF-8");
				System.out.println(content);
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		// 爬取页面
		Spider.create(new JobProcessor())
				.addUrl("https://www.runoob.com/")  //设置要爬取数据的页面
				.run(); //执行爬虫
		
		Spider.create(new JobProcessor())
				.addUrl("https://www.runoob.com/")  //设置要爬取数据的页面
				.addPipeline(new ConsolePipeline())
				.addPipeline(new JsonFilePipeline("C:\\Users\\Unenfantseul\\Documents"))
				.thread(10)//设置线程数
				.run(); //执行爬虫
	}
	
}

/**
 * Java库内Class练习参考方法
 */
public final class PracticeClass {
	/**
	 * 排序测试
	 */
	private static void sortTest() {
		byte[] dat = new byte[1024];
		byte[] sortArray = new byte[1024];
		byte[] sortArrayrev = new byte[1024];
		// 写入数组
		for (int i = 0; i < sortArray.length; i++) {
			sortArray[i] = dat[i];
			sortArrayrev[i] = dat[i];
		}
		// 排序
		Arrays.sort(sortArray);
		// 输出
		System.out.println(Arrays.toString(sortArray));
		System.out.println(Arrays.toString(sortArrayrev));
	}
	
	/**
	 * @param seed 种子
	 * @return 返回一个int类型随机数
	 */
	private static int randomClass(int seed) {
		Random rand = new Random(seed);
		return rand.nextInt();
	}
	
	/**
	 * 将字节数组转换为String类型哈希值
	 *
	 * @param bytes 字节数组
	 * @return 哈希值
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}
	
	/**
	 * mysql数据库查询、插入命令参考代码
	 * 数据库执行函数介绍
	 * executeQuery() 一般执行SELECT语句，返回ResultSet
	 * executeUpdate() 一般执行DELETE或UPDATE或INSERT语句（这些操作更新数据库，所以是update），返回int，被改变的语句的行数。
	 * execute() 不确定是什么类型的SQL语句时可以用这个方法
	 */
	private static void mysqlDriverClass() {
		// 1.用户信息和url
		String url = "jdbc:mysql://localhost:3306/";
		String username = "root";
		String password = "Zxcvbnm123++";
		
		// 连接对象
		Connection connection = null;
		// sql对象
		Statement statement = null;
		// 预编译查询
		PreparedStatement preparedStatement = null;
		// 结果集
		ResultSet resultSet = null;
		try {
			// 连接数据库
			connection = DriverManager.getConnection(url, username, password);
			// 执行SQL对象Statement，执行SQL的对象
			statement = connection.createStatement();
		} catch (SQLException exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
		
		// 执行SQL的对象去执行SQL，返回结果集
		String sql = "SELECT *FROM test.main_table;";
		try {
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				System.out.println("id=" + resultSet.getString("id") + " check=" + resultSet.getString("check"));
			}
		} catch (SQLException exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
		
		// 执行SQL的对象去执行SQL，返回结果集
		sql = "select * from test.main_table where id=" + 10001;
		try {
			resultSet = statement.executeQuery(sql);
			if (!resultSet.next()) {
				sql = "insert into test.main_table values(" + 10001 + ",'test2')";
				int rowNumber = statement.executeUpdate(sql);
				// 输出修行数
				System.out.println("命令修改行数:" + rowNumber);
			}
		} catch (SQLException exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
		
		// 释放连接
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
		
	}
	
	/**
	 * minio参考代码
	 * 1.客户端建立连接
	 * 2.以文件流的形式上传文件
	 * 3.指定一个GET请求返回对应文件URL
	 */
	private static void minioDriverClass() {
		// 服务器相关信息
		String serverUrl = "http://192.168.1.21:9000";
		String ACCESS_KEY = "k7rAIrfc42MjtBLuAUQX";
		String SECRET_KEY = "uZcEXKKhDExQkXCRGdDs7x79G2TOLzqLL6hcxaM1";
		
		try {
			// 初始化客户端
			// 使用密钥与地址建立连接
			MinioClient minioClient = MinioClient.builder()
					.endpoint(serverUrl)
					.credentials(ACCESS_KEY, SECRET_KEY)
					.build();
			
			// 创建InputStream上传
			File file = new File("D:\\TestPhoto\\Hikari Yuka[ROGLE] Vol.1 [63P]\\0002.jpg");
			InputStream fileInputStream = new FileInputStream(file);
			// 上传
			minioClient.putObject(PutObjectArgs.builder()
										  .bucket("tesbucket")
										  .object("00001/" + file.getName())
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
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception);
		}
	}
	
	/**
	 * CRC校验测试
	 */
	private static void CRCdemo() {
		Random rand = new Random();
		// 创建空数组
		byte[] dat = new byte[1024];
		// 校验结果
		String[] crcValue = new String[14];
		// 随机数赋值
		for (int i = 0; i < dat.length; i++) {
			dat[i] = (byte) rand.nextInt(255);
		}
		System.out.println(Arrays.toString(dat));
		// 将计算结果转换为大写字符串并添加前缀0x
		crcValue[0] = "0x" + toHexString(Cyclic_redundancy_check.CRC8(dat)).toUpperCase();
		crcValue[1] = "0x" + toHexString(Cyclic_redundancy_check.CRC8_DARC(dat)).toUpperCase();
		crcValue[2] = "0x" + toHexString(Cyclic_redundancy_check.CRC8_ITU(dat)).toUpperCase();
		crcValue[3] = "0x" + toHexString(Cyclic_redundancy_check.CRC8_MAXIM(dat)).toUpperCase();
		crcValue[4] = "0x" + toHexString(Cyclic_redundancy_check.CRC8_ROHC(dat)).toUpperCase();
		crcValue[5] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_CCITT(dat)).toUpperCase();
		crcValue[6] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_CCITT_FALSE(dat)).toUpperCase();
		crcValue[7] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_XMODEM(dat)).toUpperCase();
		crcValue[8] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_X25(dat)).toUpperCase();
		crcValue[9] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_MODBUS(dat)).toUpperCase();
		crcValue[10] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_IBM(dat)).toUpperCase();
		crcValue[11] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_MAXIM(dat)).toUpperCase();
		crcValue[12] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_USB(dat)).toUpperCase();
		crcValue[13] = "0x" + toHexString(Cyclic_redundancy_check.CRC16_DNP(dat)).toUpperCase();
		System.out.println(Arrays.toString(crcValue));
	}
	
	private static void modifyExifDemo() {
		try {
			//读文件
			File file = new File("C:\\demo.jpg");
			//获取ImageMetadata对象实例
			ImageMetadata metadata = Imaging.getMetadata(file);
			//强转为JpegImageMetadata
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			//获取TiffImageMetadata
			TiffImageMetadata exif = jpegMetadata.getExif();
			//转换为流
			TiffOutputSet out = exif.getOutputSet();
			//获取TiffOutputDirectory
			TiffOutputDirectory exifDirectory = out.getOrCreateExifDirectory();
			//移除拍摄时间
			exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
			exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
			//初始化时间
			//String date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
			//添加拍摄时间 格式为"yyyy:MM:dd HH:mm:ss"
			exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, "1999:01:01 01:01:01");
			exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED, "1999:01:01 01:01:01");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\demo1.jpg"));
			//写入新的图片
			new ExifRewriter().updateExifMetadataLossless(file, bos, out);
		} catch (Exception e) {
			//很多图片可能读取exif出现异常为正常现象 通常无需处理
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试主函数
	 *
	 * @param args 输入参数
	 */
	public static void main(final String[] args) {
		System.out.println("Hello World");
	}
}
