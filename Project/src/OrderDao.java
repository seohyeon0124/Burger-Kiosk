import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "sqlid";
	private String pw = "sqlpw";
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	int cnt = -1;

	public OrderDao() {
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
	
	// 1. 주문내역 전체 가져오기
	public ArrayList<OrderMenuBean> selectAllOrderMenu() {
		connect();
		ArrayList<OrderMenuBean> lists = new ArrayList<OrderMenuBean>();
		try {
			String sql = "select * from orderMenu order by orderNum";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				int orderNum = rs.getInt("orderNum");
				String orderMenu = rs.getString("orderMenu");
				int orderCount = rs.getInt("orderCount");
				int orderPrice = rs.getInt("orderPrice");
				String orderDay = String.valueOf(rs.getDate("orderDay"));
				OrderMenuBean ob = new OrderMenuBean();
				ob.setOrderNum(orderNum);
				ob.setOrderMenu(orderMenu);
				ob.setOrderCount(orderCount);
				ob.setOrderPrice(orderPrice);
				ob.setOrderDay(orderDay);
				lists.add(ob);
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

	// 2. 주문번호 최대 구하기
	public int orderNum() {
		connect();
		int num = 0;
		String sql = "select max(orderNum) num from orderMenu";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next())
				num = rs.getInt("num");
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
		return num;
	}
	
	// 3. 주문하기
	public void insert(int orderNum, String orderMenu, int orderCount, int orderPrice) {
		connect();
        try {
            String sql = "INSERT INTO orderMenu(orderNum,orderMenu,orderCount,orderPrice) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderNum);
            ps.setString(2, orderMenu);
            ps.setInt(3, orderCount);
            ps.setInt(4, orderPrice);
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
            String sql = "delete from orderMenu where orderNum=?";
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
