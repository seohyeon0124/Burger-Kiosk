import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilterDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "sqlid";
	private String pw = "sqlpw";
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	int cnt = -1;

	public FilterDao() {
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

	public ArrayList<OrderMenuBean> filtOrderMenu_Day() {
		connect();
		ArrayList<OrderMenuBean> lists = new ArrayList<OrderMenuBean>();
		try {
	        String sql = "SELECT * FROM ordermenu WHERE TO_CHAR(orderDay, 'YYYY-MM-DD') = TO_CHAR(TRUNC(SYSDATE), 'YYYY-MM-DD')";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				OrderMenuBean b = new OrderMenuBean();
				b.setOrderNum(rs.getInt("orderNum"));
				b.setOrderMenu(rs.getString("orderMenu"));
				b.setOrderCount(rs.getInt("orderCount"));
				b.setOrderPrice(rs.getInt("orderPrice"));
				b.setOrderDay(String.valueOf(rs.getDate("orderDay")));
				lists.add(b);
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
	public ArrayList<OrderMenuBean> filtOrderMenu_Week() {
		connect();
		ArrayList<OrderMenuBean> lists = new ArrayList<OrderMenuBean>();
		try {
			String sql = "select * from ordermenu WHERE orderDay >= TRUNC(SYSDATE, 'IW') "
					+ "AND orderDay < TRUNC(SYSDATE, 'IW') + 7";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				OrderMenuBean b = new OrderMenuBean();
				b.setOrderNum(rs.getInt("orderNum"));
				b.setOrderMenu(rs.getString("orderMenu"));
				b.setOrderCount(rs.getInt("orderCount"));
				b.setOrderPrice(rs.getInt("orderPrice"));
				b.setOrderDay(String.valueOf(rs.getDate("orderDay")));
				lists.add(b);
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
	public ArrayList<OrderMenuBean> filtOrderMenu_Month() {
		connect();
		ArrayList<OrderMenuBean> lists = new ArrayList<OrderMenuBean>();
		try {
			String sql = "select * from ordermenu WHERE orderDay >= TRUNC(SYSDATE, 'MM') "
					+ "AND orderDay < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				OrderMenuBean b = new OrderMenuBean();
				b.setOrderNum(rs.getInt("orderNum"));
				b.setOrderMenu(rs.getString("orderMenu"));
				b.setOrderCount(rs.getInt("orderCount"));
				b.setOrderPrice(rs.getInt("orderPrice"));
				b.setOrderDay(String.valueOf(rs.getDate("orderDay")));
				lists.add(b);
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

	public ArrayList<MenuBean> filtMenu(String filt) {
		connect();
		ArrayList<MenuBean> lists = new ArrayList<MenuBean>();
		try {
			String sql = "select * from menu where menuGroup=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,filt);
			rs = ps.executeQuery();
			while(rs.next()) {
				MenuBean b = new MenuBean();
				b.setMenuNum(rs.getString("menuNum"));
				b.setMenuGroup(rs.getString("menuGroup"));
				b.setMenuName(rs.getString("menuName"));
				b.setMenuPrice(rs.getInt("menuPrice"));
				b.setSoldOut(rs.getString("soldOut"));
				lists.add(b);
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

	public ArrayList<CouponBean> filtCoupon(String filt) {
		connect();
		ArrayList<CouponBean> lists = new ArrayList<CouponBean>();
		try {
			String sql = "select * from coupon where usingCoupon=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, filt);
			rs = ps.executeQuery();
			while(rs.next()) {
				CouponBean b = new CouponBean();
				b.setCouponNum(rs.getString("couponNum"));
				b.setMenuName(rs.getString("menuName"));
				b.setCouponExpirationDate(rs.getString("couponExpirationDate"));
				b.setUsingCoupon(rs.getString("usingCoupon"));
				lists.add(b);
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

}
