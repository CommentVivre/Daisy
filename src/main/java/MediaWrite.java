import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Random;

/*
 * 媒体文件写入
 * 输入媒体文件 写真集名称 作者
 * 1.获取媒体文件信息 与作者名称、写真集一起 写入数据库
 * 2.将文件写入对象存储
 */
public final class MediaWrite {
	
	private static Connection mysqlConnection;
	private static Statement mysqlStatement;
	private static boolean databaseLinkStatus;
	private static ResultSet resultSet;
	private static Random rand;
	
	/**
	 * 创建数据库连接
	 *
	 * @param mysqlInfo 数据库连接信息
	 *                  0:url 1:username 2:password
	 * @param minioInfo 对象存储连接信息
	 */
	public static void createMediaLink(String @NotNull [] mysqlInfo, String @NotNull [] minioInfo) {
		// 创建数据库链接
		try {
			// 输入信息 打开Mysql连接
			mysqlConnection = DriverManager.getConnection(mysqlInfo[0], mysqlInfo[1], mysqlInfo[2]);
			mysqlStatement = mysqlConnection.createStatement();
		} catch (SQLException e) {
			// 初始化失败 抛出错误
			databaseLinkStatus = false;
			throw new RuntimeException(e);
		}
		// 随机数生成
		rand = new Random(1024);
		
		// 连接创建成功
		databaseLinkStatus = true;
	}
	
	/**
	 * 释放资源 关闭数据库连接
	 */
	public static void closeMediaLink() {
		try {
			resultSet.close();
			mysqlStatement.close();
			mysqlConnection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	// 生成数据表列id 并保证不重复
	private static int dataBaseNumber(String tableName) {
		int number = 0;
		// 查询SQL语句 待替换数据库名与表名
		try {
			do {
				number = rand.nextInt();
				String querySql = "select * from " + tableName + " where id = " + number + ";";
				resultSet = mysqlStatement.executeQuery(querySql);
			} while (resultSet.next());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return number;
	}
	
	
	public static void selectDataBase(String targetTable) {
	
	}
	
	// 写入文件表
	public static void insertFileTable(String[] fileInfo) throws SQLException {
		if (databaseLinkStatus) {
			return;
		}
		int targetIndex = 0;
		targetIndex = dataBaseNumber("database.table");
		// 插入数据
	}
	
	// 测试主函数代码
	public static void main(String[] args) {
		String[] mysqlInfo = {"jdbc:mysql://localhost:3306/", "root", "Zxcvbnm123++"};
		String[] minioInfo = {"void", "void"};
		createMediaLink(mysqlInfo, minioInfo);
		System.out.println(dataBaseNumber("test.main_table"));
		closeMediaLink();
	}
}
