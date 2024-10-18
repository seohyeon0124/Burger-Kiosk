import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String id = "sqlid";
	private String pw = "sqlpw";
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	int cnt = -1;

	public MenuDao() {
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
	
	// 1. 메뉴분류별로 상품 가져오기 (상품코드기준 정렬)
	public ArrayList<MenuBean> getMenuItems(String menuGroup){ //dao.getMenuItems("세트")
		connect();
		ArrayList<MenuBean> lists = new ArrayList<MenuBean>();
		try {
			String sql = "select * from menu where menuGroup like ? order by menuNum";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuGroup);
			rs = ps.executeQuery();
			while(rs.next()) {
				MenuBean menubean = new MenuBean();
				menubean.setMenuName(rs.getString("menuName"));
				menubean.setMenuPrice(rs.getInt("menuPrice"));
				lists.add(menubean);
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
	
	// 2. 주문추가(선택한 메뉴 정보 가져오기)
	public ArrayList<MenuBean> getTheItem(Object menuItems) {
		connect();
		ArrayList<MenuBean> lists = new ArrayList<MenuBean>();
		try {
			String sql = "select * from menu where menuName=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuItems.toString());
			rs = ps.executeQuery();
			while(rs.next()) {
				MenuBean menubean = new MenuBean();
				menubean.setMenuNum(rs.getString("menuNum"));
				menubean.setMenuGroup(rs.getString("menuGroup"));
				menubean.setMenuName(rs.getString("menuName"));
				menubean.setMenuPrice(rs.getInt("menuPrice"));
				lists.add(menubean);
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

	// 3. 주문하기버튼
	public void insertOrder(String orderMenu, String orderPrice) {
		connect();
		try {
			String sql = "insert into orderMenu(orderNum,orderMenu,orderPrice) values(orderseq.nextval,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderMenu);
			ps.setString(2, orderPrice);
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
	
	// 4. 메뉴전부가져오기
	public ArrayList<MenuBean> selectAllMenu() {
		connect();
		ArrayList<MenuBean> lists = new ArrayList<MenuBean>();
		try {
			String sql = "select * from menu order by menuNum";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				String menuNum = rs.getString("menuNum");
				String menuGroup = rs.getString("menuGroup");
				String menuName = rs.getString("menuName");
				int menuPrice = rs.getInt("menuPrice");
				String soldOut = rs.getString("soldOut");
				MenuBean mb = new MenuBean();
				mb.setMenuNum(menuNum);
				mb.setMenuGroup(menuGroup);
				mb.setMenuName(menuName);
				mb.setMenuPrice(menuPrice);
				mb.setSoldOut(soldOut);
				lists.add(mb);
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

	public void insert(String menuNum, String menuGroup, String menuName, int menuPrice) {
		connect();
		String sql = "insert into menu(menuNum,menuGroup,menuName,menuPrice) values(?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuNum);
			ps.setString(2, menuGroup);
			ps.setString(3, menuName);
			ps.setInt(4, menuPrice);
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

	public void update(String menuNum, String menuGroup, String menuName, int menuPrice) {
		connect();
		String sql = "update menu set menuGroup=?,menuName=?,menuPrice=? where menuNum=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuGroup);
			ps.setString(2, menuName);
			ps.setInt(3, menuPrice);
			ps.setString(4, menuNum);
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
            String sql = "delete from menu where menuNum=?";
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

	public String getMenuNum(String menuGroup) {
		String menuNum = "auto";
		String code = null;
		connect();
		try {
			String sql = "select max(menuNum) num from menu where menuGroup=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, menuGroup);
			rs = ps.executeQuery();
			if(rs.next()) {
				String num = rs.getString("num");
				if (num != null && !num.isEmpty()) {
	                // 숫자 부분만 추출합니다.
	                int numPart = Integer.parseInt(num.replaceAll("\\D+", ""));
	                // 숫자 부분에 1을 더합니다.
	                numPart++;
	                switch(menuGroup) {
	                case "하우스스페셜" : code="ag"; break;
	                case "세트" : code="bg"; break;
	                case "단품" : code="cg"; break;
	                case "사이드" : code="dg"; break;
	                case "샐러드" : code="eg"; break;
	                case "음료" : code="fg"; break;
	                }
	                menuNum = code + String.format("%02d", numPart);
	            }
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
		return menuNum;
	}
}
