import java.sql.*;

public class JDBC {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// 2.用户信息和url
		String url = "jdbc:mysql://localhost:3306/";
		String username = "root";
		String password = "Zxcvbnm123++";
		// 3.连接成功，数据库对象 Connection
		Connection connection = DriverManager.getConnection(url, username, password);
		// 4.执行SQL对象Statement，执行SQL的对象
		Statement statement = connection.createStatement();
		
		// 5.执行SQL的对象去执行SQL，返回结果集
		String sql = "SELECT *FROM test.main_table;";
		
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			System.out.println("id=" + resultSet.getString("id") + " check=" + resultSet.getString("check"));
		}
		/*
		 * executeQuery()，一般执行SELECT语句，返回ResultSet
		 * executeUpdate()，一般执行DELETE或UPDATE或INSERT语句（这些操作更新数据库，所以是update），返回int，被改变的语句的行数。
		 * execute()，不确定是什么类型的SQL语句时可以用这个方法。
		 */
		for (int i = 4; i <= 25; i++) {
			sql = "select * from test.main_table where id=" + i;
			resultSet = statement.executeQuery(sql);
			
			if (!resultSet.next()) {
				sql = "insert into test.main_table values(" + i + ",'test2')";
				statement.executeUpdate(sql);
			}
		}
		//  6.释放连接
		resultSet.close();
		statement.close();
		connection.close();
	}
}

