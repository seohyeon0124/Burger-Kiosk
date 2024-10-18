import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CouponDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "sqlid";
	private String pw = "sqlpw";
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	int cnt = -1;

	public CouponDao() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		try {
			conn = DriverManager.getConnection(url,id,pw);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 쿠폰 전체 가져오기
	public ArrayList<CouponBean> selectAllCoupon() {
		connect();
		ArrayList<CouponBean> lists = new ArrayList<CouponBean>();
		try {
			String sql = "select * from coupon";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				String couponNum = rs.getString("couponNum");
				String menuName = rs.getString("menuName");
				String couponExpirationDate = String.valueOf(rs.getDate("couponExpirationDate"));
				String usingCoupon = rs.getString("usingCoupon");
				CouponBean cb = new CouponBean();
				cb.setCouponNum(couponNum);
				cb.setMenuName(menuName);
				cb.setCouponExpirationDate(couponExpirationDate);
				cb.setUsingCoupon(usingCoupon);
				lists.add(cb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lists;
	}

	// 쿠폰사용
	public int getCouponNum(String couponNum) {
		connect();
		try {
			String sql = "select count(*) from coupon where upper(couponNum)=upper(?) and usingCoupon='O'";
			ps = conn.prepareStatement(sql);
			ps.setString(1, couponNum);
			rs = ps.executeQuery();
			if(rs.next() && rs.getInt(1) > 0) // count(*)의 결과가 0보다 큰 경우
				cnt = 1; // 사용 가능한 쿠폰
			else
				cnt = 0; // 사용 불가능한 쿠폰
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}
	
	public void insert(String couponNum, String menuName, String couponExpirationDate) {
		connect();
		String sql = "insert into coupon(couponNum,menuName,couponExpirationDate) values(?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, couponNum);
			ps.setString(2, menuName);
			ps.setString(3, couponExpirationDate);
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(String couponNum, String menuName, String couponExpirationDate) {
		connect();
		String sql = "update coupon set menuName=?,couponExpirationDate=? where couponNum=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuName);
			ps.setString(2, couponExpirationDate);
			ps.setString(3, couponNum);
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void delete(String objNum) {
		connect();
        try {
            String sql = "delete from coupon where couponNum=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, objNum);
            cnt = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			try {
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
