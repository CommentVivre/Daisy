import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Integer.toHexString;

/*
 * 测试程序入口
 */
public class Main {
	// 创建空数组
	static byte[] dat = new byte[1024];
	// 校验结果
	static String[] crcValue = new String[14];
	
	// 数组赋值随机数
	private static void randomFillArray() {
		Random rand = new Random();
		for (int i = 0; i < dat.length; i++) {
			dat[i] = (byte) rand.nextInt(255);
		}
		System.out.println(Arrays.toString(dat));
	}
	
	// 校验测试
	private static void CRCTest() {
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
		return;
	}
	
	// 排序测试
	private static void sortTest() {
		byte[] sortArray = new byte[1024];
		byte[] sortArrayrev = new byte[1024];
		// 写入数组
		for (int i = 0; i < sortArray.length; i++) {
			sortArray[i] = dat[i];
			sortArrayrev[i] = dat[i];
		}
		Arrays.sort(sortArray);
		
		System.out.println(Arrays.toString(sortArray));
		System.out.println(Arrays.toString(sortArrayrev));
	}
	
	public void modifyExifDemo() {
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
	 * 计算SHA-512哈希值
	 * @param filePath 文件路径
	 * @return 字节数组
	 * @throws IOException IO异常
	 * @throws NoSuchAlgorithmException NoSearch算法异常
	 */
	public static byte[] calculateSHA512(String filePath) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		try (
				FileInputStream fis = new FileInputStream(filePath);
				FileChannel channel = fis.getChannel();
				DigestInputStream dis = new DigestInputStream(fis, digest))
        {
            ByteBuffer buffer = ByteBuffer.allocate(81920); // 80 KB buffer
			while (channel.read(buffer) != -1) {
				buffer.flip();
				digest.update(buffer);
				buffer.clear();
			}
			return digest.digest();
		}
	}

	/**
	 * 将字节数组转换为String类型哈希值
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

	public static void main(String[] args) {
		String filePath = "D:\\TestPhoto\\Hikari Yuka[ROGLE] Vol.1 [63P]\\0001.jpg";
		try {
			byte[] sha256 = calculateSHA512(filePath);
			String sha256Hex = bytesToHex(sha256);
			System.out.println("SHA512: " + sha256Hex);
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		FileWrite.minioTest();
		Random rand = new Random(1024);
		for (int i = 0; i < 10; i++) System.out.println(rand.nextInt());
	}
}

