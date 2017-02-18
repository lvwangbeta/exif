package exif;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;


public class QueryHelper {

	private Connection con;
	
	public QueryHelper() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/exif", "root", "123456");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public QueryHelper save(String album_id, Image image, int type) {
		String sql = "insert into exif values(?,?,?,?,?,?,?,?,?)";
		QueryRunner query = new QueryRunner();
		try {
			query.update(con, sql, new Object[]{
												album_id,
												image.img_id,
												type,
												image.exif.lens.name,
												image.exif.camera.name,
												image.exif.lens.brand,
												image.exif.lens.length,
												image.exif.lens.aperture,
												"风光"
												});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
