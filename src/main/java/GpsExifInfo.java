import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*************************************
 *Class Name: gpsExifInfo
 *Description: <获取相机经纬度并且转换实际位置>
 *************************************/
public class GpsExifInfo {
	
	public static void main(String[] args) throws ImageProcessingException, IOException {
		
		String imgDir = "E:\\imgs";
		File[] files = new File(imgDir).listFiles();
		
		for (File file : Objects.requireNonNull(files)) {
			if (!file.getName().endsWith(".jpg")) {
				continue;
			}
			System.out.println("----------------------------------------\n" + file.getName());
			// 获取照片信息
			Map exifMap = readPicExifInfo(file);
			// 打印照片信息
			printPicExifInfo(exifMap);
		}
	}
	
	/**
	 * 获取图片文件的Exif信息
	 *
	 * @param file
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	private static Map<String, String> readPicExifInfo(File file) throws ImageProcessingException, IOException {
		Map<String, String> map = new HashMap<>();
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				// 输出所有属性
				System.out.format(
						"[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());
				map.put(tag.getTagName(), tag.getDescription());
			}
			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					System.err.format("ERROR: %s", error);
				}
			}
		}
		return map;
	}
	
	/**
	 * 打印照片Exif信息
	 *
	 * @param map
	 */
	private static void printPicExifInfo(Map<String, String> map) {
		String[] strings = new String[]{"Compression", "Image Width", "Image Height", "Make", "Model", "Software",
				"GPS Version ID", "GPS Latitude", "GPS Longitude", "GPS Altitude", "GPS Time-Stamp", "GPS Date Stamp",
				"ISO Speed Ratings", "Exposure Time", "Exposure Mode", "F-Number", "Focal Length 35", "Color Space", "File Source", "Scene Type"};
		String[] names = new String[]{"压缩格式", "图像宽度", "图像高度", "拍摄手机", "型号", "手机系统版本号",
				"gps版本", "经度", "纬度", "高度", "UTC时间戳", "gps日期",
				"iso速率", "曝光时间", "曝光模式", "光圈值", "焦距", "图像色彩空间", "文件源", "场景类型"};
		
		for (int i = 0; i < strings.length; i++) {
			if (map.containsKey(strings[i])) {
				System.out.println(names[i] + "  " + strings[i] + " : " + map.get(strings[i]));
			}
		}
	}
}
