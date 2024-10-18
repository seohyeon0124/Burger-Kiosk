import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Admin implements ActionListener, ItemListener {

	MenuDao mdao = new MenuDao();
	OrderDao odao = new OrderDao();
	CouponDao cdao = new CouponDao();
	FilterDao fdao = new FilterDao();
	SortDao sdao = new SortDao();

	JFrame adminLogin = new JFrame();
	JLabel adminPW = new JLabel("관리자 비밀번호 : ");
	JTextField pwInput = new JTextField();
	JFrame adminFrame = null; // 로그인성공 -> 관리자화면
	JFrame errorFrame = null; // 로그인실패 -> 비밀번호틀립니다
	JTable adminTable;

	public void adminLogin() {
		adminLogin.setLayout(null);
		adminLogin.add(adminPW);
		adminLogin.add(pwInput);
		adminPW.setBounds(40,25,100,100);
		pwInput.setBounds(150,60,200,30);
		pwInput.addActionListener(this);
		adminLogin.setTitle("키오스크(관리자)");
		adminLogin.setSize(400,200);
		adminLogin.setLocationRelativeTo(null);
		adminLogin.setResizable(false);
		adminLogin.setVisible(true);
	}

	JTabbedPane adminTabbedPane = new JTabbedPane();
	String[] adminGroup = {"주문관리", "메뉴관리", "쿠폰관리"};
	JPanel panel = new JPanel();
	Choice filter = new Choice();
	Choice sort = new Choice();
	JButton insertBtn = new JButton("추가");
	JButton updateBtn = new JButton("변경");
	JButton deleteBtn = new JButton("삭제");
	String check = adminGroup[0];
	String[] columnNames;
	Object[][] rowData;
	DefaultTableModel model;
	JScrollPane adminScrollPane;
	double price = 0;
	JLabel totalPrice = new JLabel("매출 : " + String.valueOf(price));
	public void adminFrame() {
		adminFrame = new JFrame();
		String[] f,s;
		f = new String[]{"필터","오늘","이번주","이번달"};
		for(int i=0; i<f.length; i++) {
			filter.add(f[i]);
		}
		sort.removeAll();
		s = new String[]{"정렬","주문번호","주문내역","주문날짜"};
		for(int i=0; i<s.length; i++) {
			sort.add(s[i]);
		}
		panel.add(deleteBtn);
		panel.add(totalPrice);
		for(int i=0; i < adminGroup.length; i++) {
			if(adminTabbedPane.indexOfTab(adminGroup[i]) == -1) { // 이미 탭에 해당 이름이 없는 경우에만 추가
				adminTable = new JTable();
				adminScrollPane = new JScrollPane(adminTable);
				switch(adminGroup[i]) {
				case "주문관리" :
					order();
					break;
				case "메뉴관리" :
					menu();
					break;
				case "쿠폰관리" :
					coupon();
					break;
				}
				adminTabbedPane.addTab(adminGroup[i], adminScrollPane);
				model = new DefaultTableModel(rowData, columnNames);
				adminTable.setModel(model);
				adminScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				adminScrollPane.getVerticalScrollBar().setUnitIncrement(20);
				total();
			}
		}
		adminTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) { 
				// 스크롤바 맨위로
				JScrollPane currentScrollPane = (JScrollPane) adminTabbedPane.getSelectedComponent();
				currentScrollPane.getVerticalScrollBar().setValue(0);
				String[] f = null;
				String[] s = null;
				int selectedIndex = adminTabbedPane.getSelectedIndex();
				filter.removeAll();
				sort.removeAll();
				if(selectedIndex == 0) {
					sort.select("정렬");
					order();
					refreshTable();
					check = adminGroup[0];
					f = new String[]{"필터","오늘","이번주","이번달"};
					s = new String[]{"정렬","주문번호","주문내역","주문날짜"};
					panel.remove(insertBtn);
					panel.remove(updateBtn);
					panel.add(totalPrice);
					panel.add(deleteBtn);
				} else if(selectedIndex == 1) {
					sort.select("정렬");
					menu();
					refreshTable();
					check = adminGroup[1];
					f = new String[]{"필터","하우스스페셜","세트","단품","사이드","샐러드","음료"};
					s = new String[]{"정렬","상품코드","상품분류","상품명","상품가격"};
					panel.remove(totalPrice);
					panel.add(insertBtn);
					panel.add(updateBtn);
					panel.add(deleteBtn);
				} else if(selectedIndex == 2) {
					sort.select("정렬");
					coupon();
					refreshTable();
					check = adminGroup[2];
					f = new String[]{"필터","사용가능","사용불가능"};
					s = new String[]{"정렬","쿠폰번호","상품명","유효기간"};
					panel.remove(totalPrice);
					panel.add(insertBtn);
					panel.add(updateBtn);
					panel.add(deleteBtn);
				}
				for(int i=0; i<f.length; i++) {
					filter.add(f[i]);
				}
				for(int i=0; i<s.length; i++) {
					sort.add(s[i]);
				}
			}
		});
		adminFrame.setTitle("관리자");
		adminFrame.setLayout(null);
		adminFrame.setSize(550, 450);
		adminFrame.setLocationRelativeTo(null);
		adminFrame.add(adminTabbedPane);
		adminTabbedPane.setBounds(0,0,536,350);
		adminFrame.add(panel);
		panel.setBounds(0, 350, 536, 100);
		panel.setLayout(null);
		insertBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		panel.add(filter);
		panel.add(sort);
		filter.addItemListener(this);
		sort.addItemListener(this);
		insertBtn.setBounds(280, 10, 80, 40);
		updateBtn.setBounds(360, 10, 80, 40);
		deleteBtn.setBounds(440, 10, 80, 40);
		totalPrice.setBounds(320,10,150,40);
		filter.setBounds(20,20,80,40);
		sort.setBounds(100,20,80,40);
		adminFrame.setResizable(false);
		adminFrame.setVisible(true);
	}

	private void order() {
		columnNames = new String[]{"주문번호","주문내역","주문수량","주문금액","주문날짜"};
		ArrayList<OrderMenuBean> orderLists = odao.selectAllOrderMenu();
		rowData = new Object[orderLists.size()][columnNames.length];
		for(int j=0; j<orderLists.size(); j++) {
			int k=0;
			rowData[j][k++] = orderLists.get(j).getOrderNum();
			rowData[j][k++] = orderLists.get(j).getOrderMenu();
			rowData[j][k++] = orderLists.get(j).getOrderCount();
			rowData[j][k++] = orderLists.get(j).getOrderPrice();
			rowData[j][k++] = orderLists.get(j).getOrderDay();
		}
	}

	private void menu() {
		columnNames = new String[]{"상품코드","상품분류","상품명","상품가격","품절여부"};
		ArrayList<MenuBean> menuLists = mdao.selectAllMenu();
		rowData = new Object[menuLists.size()][columnNames.length];
		for(int j=0; j<menuLists.size(); j++) {
			int k=0;
			rowData[j][k++] = menuLists.get(j).getMenuNum();
			rowData[j][k++] = menuLists.get(j).getMenuGroup();
			rowData[j][k++] = menuLists.get(j).getMenuName();
			rowData[j][k++] = menuLists.get(j).getMenuPrice();
			rowData[j][k++] = menuLists.get(j).getSoldOut();
		}
	}

	private void coupon() {
		columnNames = new String[]{"쿠폰코드","상품명","유효기간","사용가능여부"};
		ArrayList<CouponBean> couponLists = cdao.selectAllCoupon();
		rowData = new Object[couponLists.size()][columnNames.length];
		for(int j=0; j<couponLists.size(); j++) {
			int k=0;
			rowData[j][k++] = couponLists.get(j).getCouponNum();
			rowData[j][k++] = couponLists.get(j).getMenuName();
			rowData[j][k++] = couponLists.get(j).getCouponExpirationDate();
			rowData[j][k++] = couponLists.get(j).getUsingCoupon();
		}
	}

	private void refreshTable() {
		int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView(); // JScrollPane에서 JTable 가져오기
		// 현재 선택된 탭에 따라 적절한 데이터 로드 메소드 호출
		switch(selectedIndex) {
		case 0: order(); break;
		case 1: menu(); break;
		case 2: coupon(); break;
		}
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}

	public void errorFrame(String word,int a, int b, int c, int d) {
		JFrame errorF = new JFrame();
		JLabel errorL = new JLabel(word);
		JButton errorB = new JButton("확인");
		errorB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				errorF.dispose();
			}
		});
		errorF.setLayout(null);
		errorF.add(errorL);
		errorF.add(errorB);
		errorL.setBounds(a,b,c,d);
		errorB.setBounds(80, 70, 80, 30);
		errorF.setSize(200,100);
		errorF.setBounds(0, 0,250,150);
		errorF.setResizable(false);
		errorF.setLocationRelativeTo(null);
		errorF.setVisible(true);
	}
	
	private void getFilterOrderMenu(ArrayList<OrderMenuBean> lists) {
		int selectedIndex = adminTabbedPane.getSelectedIndex();
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView();
		
		columnNames = new String[]{"주문번호","주문내역","주문수량","주문금액","주문날짜"};
		rowData = new Object[lists.size()][columnNames.length];
		for(int j=0; j<lists.size(); j++) {
			int k=0;
			rowData[j][k++] = lists.get(j).getOrderNum();
			rowData[j][k++] = lists.get(j).getOrderMenu();
			rowData[j][k++] = lists.get(j).getOrderCount();
			rowData[j][k++] = lists.get(j).getOrderPrice();
			rowData[j][k++] = lists.get(j).getOrderDay();
		}
		
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}
	
	private void getFilterMenu(String filt) {
		int selectedIndex = adminTabbedPane.getSelectedIndex();
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView();
		
		columnNames = new String[]{"상품코드","상품분류","상품명","상품가격","품절여부"};
		ArrayList<MenuBean> lists = fdao.filtMenu(filt);
		rowData = new Object[lists.size()][columnNames.length];
		for(int j=0; j<lists.size(); j++) {
			int k=0;
			rowData[j][k++] = lists.get(j).getMenuNum();
			rowData[j][k++] = lists.get(j).getMenuGroup();
			rowData[j][k++] = lists.get(j).getMenuName();
			rowData[j][k++] = lists.get(j).getMenuPrice();
			rowData[j][k++] = lists.get(j).getSoldOut();
		}
		
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}
	
	private void getFilterCoupon(String filt) {
		int selectedIndex = adminTabbedPane.getSelectedIndex();
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView();
		
		columnNames = new String[]{"쿠폰코드","상품명","유효기간","사용가능여부"};
		ArrayList<CouponBean> lists = fdao.filtCoupon(filt);
		rowData = new Object[lists.size()][columnNames.length];
		for(int j=0; j<lists.size(); j++) {
			int k=0;
			rowData[j][k++] = lists.get(j).getCouponNum();
			rowData[j][k++] = lists.get(j).getMenuName();
			rowData[j][k++] = lists.get(j).getCouponExpirationDate();
			rowData[j][k++] = lists.get(j).getUsingCoupon();
		}
		
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}
	

	public void getSortOrderMenu(String sort) {
		int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView(); // JScrollPane에서 JTable 가져오기
		columnNames = new String[]{"주문번호","주문내역","주문수량","주문금액","주문날짜"};
		ArrayList<OrderMenuBean> lists = sdao.sortOrderMenu(sort);
		rowData = new Object[lists.size()][columnNames.length];
		for(int j=0; j<lists.size(); j++) {
			int k=0;
			rowData[j][k++] = lists.get(j).getOrderNum();
			rowData[j][k++] = lists.get(j).getOrderMenu();
			rowData[j][k++] = lists.get(j).getOrderCount();
			rowData[j][k++] = lists.get(j).getOrderPrice();
			rowData[j][k++] = lists.get(j).getOrderDay();
		}
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}
	
	public void getSortMenu(String sort) {
		int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView(); // JScrollPane에서 JTable 가져오기
		columnNames = new String[]{"상품코드","상품분류","상품명","상품가격","품절여부"};
		ArrayList<MenuBean> lists = sdao.sortMenu(sort);
		rowData = new Object[lists.size()][columnNames.length];
		for(int j=0; j<lists.size(); j++) {
			int k=0;
			rowData[j][k++] = lists.get(j).getMenuNum();
			rowData[j][k++] = lists.get(j).getMenuGroup();
			rowData[j][k++] = lists.get(j).getMenuName();
			rowData[j][k++] = lists.get(j).getMenuPrice();
			rowData[j][k++] = lists.get(j).getSoldOut();
		}
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}
	
	public void getSortCoupon(String sort) {
		int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView(); // JScrollPane에서 JTable 가져오기
		columnNames = new String[]{"쿠폰코드","상품명","유효기간","사용가능여부"};
		ArrayList<CouponBean> lists = sdao.sortCoupon(sort);
		rowData = new Object[lists.size()][columnNames.length];
		for(int j=0; j<lists.size(); j++) {
			int k=0;
			rowData[j][k++] = lists.get(j).getCouponNum();
			rowData[j][k++] = lists.get(j).getMenuName();
			rowData[j][k++] = lists.get(j).getCouponExpirationDate();
			rowData[j][k++] = lists.get(j).getUsingCoupon();
		}
		model = new DefaultTableModel(rowData, columnNames);
		table.setModel(model);
	}
	
	public void total() {
		int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
		JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
		JTable table = (JTable) selectedScrollPane.getViewport().getView(); // JScrollPane에서 JTable 가져오기
		TableModel model = table.getModel();
		price=0;
		for(int i=0; i<model.getRowCount(); i++) {
			Object value = model.getValueAt(i, 3); // 인덱스 1은 두 번째 열을 의미
			// 값이 Double 타입을 예상하지만, 다른 숫자 타입이 올 수도 있으니 확인 필요
			if(value instanceof Number) {
				price += ((Number) value).doubleValue();
			} else {
				// 숫자로 변환할 수 있는지 체크 후 더하기
				try {
					double numericValue = Double.parseDouble(value.toString());
					price += numericValue;
				} catch(NumberFormatException e) {
					// 숫자 변환이 불가능한 값은 무시하거나 적절한 처리 필요
					System.err.println("Not a number: " + value);
				}
			}
			totalPrice.setText("매출 : " + (int)price);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
		if(obj==filter) {
			sort.select("정렬");
			int index = filter.getSelectedIndex();
			String filt = filter.getItem(index);
			if(selectedIndex==0) {
				if(filt.equals("필터")) {
					order();
					refreshTable();
					total();
				} else {
					if(filt.equals("오늘")) {
						getFilterOrderMenu(fdao.filtOrderMenu_Day());
						total();
					} else if(filt.equals("이번주")) {
						getFilterOrderMenu(fdao.filtOrderMenu_Week());
						total();
					} else if(filt.equals("이번달")) {
						getFilterOrderMenu(fdao.filtOrderMenu_Month());
						total();
					}
				}
			} else if(selectedIndex==1) {
				if(filt.equals("필터")) {
					menu();
					refreshTable();
				} else {
					getFilterMenu(filt);
				}
			} else if(selectedIndex==2) {
				if(filt.equals("필터")) {
					coupon();
					refreshTable();
				} else {
					if(filt.equals("사용가능")) {
						getFilterCoupon("O");
					} else if(filt.equals("사용불가능")) {
						getFilterCoupon("X");
					}
				}
			}
		}
		if(obj==sort) {
			filter.select("필터");
			int index = sort.getSelectedIndex();
			String item = sort.getItem(index);
			String sort = null;
			if(selectedIndex==0) {
				if(item.equals("정렬")) {
					order();
					refreshTable();
				} else {
					if(item.equals("주문번호")) {
						sort = "orderNum";
					} else if(item.equals("주문내역")) {
						sort = "orderMenu";
					} else if(item.equals("주문날짜")) {
						sort = "orderDay";
					}
					getSortOrderMenu(sort);
				}
			} else if(selectedIndex==1) {
				if(item.equals("정렬")) {
					menu();
					refreshTable();
				} else {
					if(item.equals("상품코드")) {
						sort = "menuNum";
					} else if(item.equals("상품분류")) {
						sort = "menuGroup";
					} else if(item.equals("상품명")) {
						sort = "menuName";
					}else if(item.equals("상품가격")) {
						sort = "menuPrice";
					}
					getSortMenu(sort);
				}
			} else if(selectedIndex==2) {
				if(item.equals("정렬")) {
					coupon();
					refreshTable();
				} else {
					if(item.equals("쿠폰번호")) {
						sort = "couponNum";
					} else if(item.equals("상품명")) {
						sort = "menuName";
					} else if(item.equals("유효기간")) {
						sort = "couponExpirationDate";
					}
					getSortCoupon(sort);
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==pwInput) {
			String pw = pwInput.getText();
			JLabel pwText = null;
			if(pw.equals("123")) {
				pwInput.setText("");
				adminFrame();
				adminLogin.setVisible(false);
			} else {
				errorFrame = new JFrame();
				pwInput.setText("");
				pwText = new JLabel("비밀번호가 맞지 않습니다");
				errorFrame.add(pwText);
				pwText.setBounds(20, 5, 150, 50);
				errorFrame.setLayout(null);
				errorFrame.setSize(200,100);
				errorFrame.setLocationRelativeTo(null);
				errorFrame.setVisible(true);
			}
		}
		if(obj==insertBtn || obj==updateBtn || obj==deleteBtn) {
			filter.select("필터");
			sort.select("정렬");
			int selectedIndex = adminTabbedPane.getSelectedIndex(); // 현재 선택된 탭의 인덱스
			JScrollPane selectedScrollPane = (JScrollPane) adminTabbedPane.getComponentAt(selectedIndex); // 선택된 탭의 JScrollPane 가져오기
			JTable table = (JTable) selectedScrollPane.getViewport().getView(); // JScrollPane에서 JTable 가져오기
			int selectedRow = table.getSelectedRow(); // JTable에서 선택된 행의 번호 가져오기
			if(selectedIndex == 0) { //주문관리
				// 주문관리 삭제 OK
				if(obj == deleteBtn) { // deleteBtn 클릭 시
					if(selectedRow >= 0) { // 테이블에서 행이 선택되었는지 확인
						Object objNum = table.getValueAt(selectedRow, 0);
						odao.delete(String.valueOf(objNum)); // 데이터베이스에서 데이터 삭제
						refreshTable();
					} else {
						errorFrame("선택해주세요",80,25,200,30);
					}
				}
			} else if(selectedIndex==1) { //메뉴관리
				// 메뉴관리 추가 OK
				if(obj==insertBtn) {
					JFrame mInsert = new JFrame();
					JButton mbtn = new JButton("등록");
					JLabel mlb1 = new JLabel("상품코드");
					JLabel mlb2 = new JLabel("상품분류");
					JLabel mlb3 = new JLabel("상품명");
					JLabel mlb4 = new JLabel("상품가격");
					JLabel mlb5 = new JLabel("이미지");
					JTextField mtf1 = new JTextField(); // mtf2에 따라 변경
					mtf1.setText("auto");
					mtf1.setEnabled(false);
					Choice mc2 = new Choice();
					mc2.add("선택");
					mc2.add("하우스스페셜");
					mc2.add("세트");
					mc2.add("단품");
					mc2.add("사이드");
					mc2.add("샐러드");
					mc2.add("음료");
					mc2.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent e) {
							// 현재 선택된 항목으로 JTextField의 내용을 변경
							mtf1.setText(mdao.getMenuNum(mc2.getSelectedItem()));
						}
					});
					JTextField mtf3 = new JTextField();
					JTextField mtf4 = new JTextField();
					JButton mbtn5 = new JButton("업로드");
					mInsert.setLayout(null);
					mInsert.add(mlb1);
					mInsert.add(mlb2);
					mInsert.add(mlb3);
					mInsert.add(mlb4);
					mInsert.add(mlb5);
					mInsert.add(mtf1);
					mInsert.add(mc2);
					mInsert.add(mtf3);
					mInsert.add(mtf4);
					mInsert.add(mbtn5);
					mInsert.add(mbtn);
					mlb1.setBounds(20, 30, 50, 30);
					mtf1.setBounds(80, 30, 130, 30);
					mlb2.setBounds(20, 70, 50, 30);
					mc2.setBounds(80, 70, 130, 30);
					mlb3.setBounds(20, 110, 50, 30);
					mtf3.setBounds(80, 110, 130, 30);
					mlb4.setBounds(20, 150, 50, 30);
					mtf4.setBounds(80, 150, 130, 30);
					mlb5.setBounds(20, 190, 50, 30);
					mbtn5.setBounds(80, 190, 130, 30);
					mbtn5.setContentAreaFilled(false);
					mbtn5.setFocusPainted(false);
					mbtn.setBounds(70, 250, 100, 40);
					mbtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String menuNum = mtf1.getText();
							String menuGroup = mc2.getSelectedItem();
							String menuName = mtf3.getText();
							String menuPrice = mtf4.getText();
							try {
								if(!menuGroup.equals("선택")) {
									if(menuName != null && !menuName.isEmpty()) {
										if(!menuPrice.equals("0")) {
											mdao.insert(menuNum, menuGroup, menuName, Integer.parseInt(menuPrice));
											refreshTable();
											mInsert.setVisible(false);
										} else {
											errorFrame("가격은 0보다 커야합니다",50,25,200,30);
										}
									} else {
										errorFrame("상품명을 입력해주세요",50,25,200,30);
									}
								} else {
									errorFrame("상품분류를 선택해주세요",50,25,200,30);
								}
							} catch(NumberFormatException ne) {
								errorFrame("가격은 숫자로만 입력해주세요",37,25,200,30);
							}
						}
					});
					mbtn5.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// 파일 선택기 생성
							JFileChooser fileChooser = new JFileChooser();
							// 파일 선택기를 '열기' 대화 상자 모드로 설정
							int returnValue = fileChooser.showOpenDialog(null);
							// 사용자가 파일을 선택하고 '열기'를 클릭한 경우
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								try {
									File selectedFile = fileChooser.getSelectedFile();
									// 선택된 파일을 읽어서 BufferedImage 객체로 생성
									BufferedImage image = ImageIO.read(selectedFile);
									// 이미지 파일을 저장할 경로와 파일 이름 지정
									File outputFile = new File("C:\\Java_jsh\\Project\\images\\" + mtf3.getText() + ".jpg");
									// 이미지를 지정된 경로에 hi.png 파일로 저장
									ImageIO.write(image, "jpg", outputFile);
									JOptionPane.showMessageDialog(null, "파일이 성공적으로 업로드되었습니다.");
								} catch (IOException ioException) {
									JOptionPane.showMessageDialog(null, "파일 업로드에 실패했습니다: " + ioException.getMessage());
								}
							}
						}
					});
					mInsert.setSize(250,350);
					mInsert.setLocationRelativeTo(null);
					mInsert.setVisible(true);
				}
				// 메뉴관리 변경 OK
				else if(obj==updateBtn) {
					if(selectedRow >= 0) {
						JFrame mUpdate = new JFrame();
						JButton mbtn = new JButton("등록");
						JLabel mlb1 = new JLabel("상품코드");
						JLabel mlb2 = new JLabel("상품분류");
						JLabel mlb3 = new JLabel("상품명");
						JLabel mlb4 = new JLabel("상품가격");
						JLabel mlb5 = new JLabel("이미지");
						JTextField mtf1 = new JTextField();
						JTextField mtf2 = new JTextField(); 
						mtf1.setEnabled(false);
						mtf2.setEnabled(false);
						JTextField mtf3 = new JTextField();
						JTextField mtf4 = new JTextField();
						JButton mbtn5 = new JButton("업로드");
						mUpdate.setLayout(null);
						mUpdate.add(mlb1);
						mUpdate.add(mlb2);
						mUpdate.add(mlb3);
						mUpdate.add(mlb4);
						mUpdate.add(mlb5);
						mUpdate.add(mtf1);
						mUpdate.add(mtf2);
						mUpdate.add(mtf3);
						mUpdate.add(mtf4);
						mUpdate.add(mbtn5);
						mUpdate.add(mbtn);
						mlb1.setBounds(20, 30, 50, 30);
						mtf1.setBounds(80, 30, 130, 30);
						mlb2.setBounds(20, 70, 50, 30);
						mtf2.setBounds(80, 70, 130, 30);
						mlb3.setBounds(20, 110, 50, 30);
						mtf3.setBounds(80, 110, 130, 30);
						mlb4.setBounds(20, 150, 50, 30);
						mtf4.setBounds(80, 150, 130, 30);
						mlb5.setBounds(20, 190, 50, 30);
						mbtn5.setBounds(80, 190, 130, 30);
						mbtn5.setContentAreaFilled(false);
						mbtn5.setFocusPainted(false);
						mbtn.setBounds(70, 250, 100, 40);
						mUpdate.setSize(250,350);
						mUpdate.setLocationRelativeTo(null);
						mUpdate.setVisible(true);
						if(selectedRow>=0) {
							updateBtn.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent e) {
									Object menuNum = table.getValueAt(selectedRow, 0);
									mtf1.setText(String.valueOf(menuNum));
									Object menuGroup = table.getValueAt(selectedRow, 1);
									mtf2.setText(String.valueOf(menuGroup));
									Object menuName = table.getValueAt(selectedRow, 2);
									mtf3.setText(String.valueOf(menuName));
									Object menuPrice = table.getValueAt(selectedRow, 3);
									mtf4.setText(String.valueOf(menuPrice));
								}
							});
							mbtn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									String menuNum = mtf1.getText();
									String menuGroup = mtf2.getText();
									String menuName = mtf3.getText();
									String menuPrice = mtf4.getText();
									try {
										if(menuName != null && !menuName.isEmpty()) {
											if(!menuPrice.equals("0")) {
												mdao.update(menuNum, menuGroup, menuName, Integer.parseInt(menuPrice));
												refreshTable();
												mUpdate.setVisible(false);
											} else {
												errorFrame("가격은 0보다 커야합니다",50,25,200,30);
											}
										} else {
											errorFrame("상품명을 입력해주세요",50,25,200,30);
										}
									} catch(NumberFormatException ne) {
										errorFrame("가격은 숫자로만 입력해주세요",37,25,200,30);
									}
								}
							});
						}
					}else {
						errorFrame("선택해주세요",80,25,200,30);
					}
				}
				// 메뉴관리 삭제 OK
				else if(obj==deleteBtn) {
					if(selectedRow >= 0) { // 테이블에서 행이 선택되었는지 확인
						Object objNum = table.getValueAt(selectedRow, 0);
						mdao.delete(String.valueOf(objNum)); // 데이터베이스에서 데이터 삭제
						refreshTable();

					} else {
						errorFrame("선택해주세요",80,25,200,30);
					}
				}
			} else if(selectedIndex==2) { //쿠폰관리
				// 쿠폰관리 추가 OK
				if(obj==insertBtn) {
					JFrame cInsert = new JFrame();
					JButton cbtn = new JButton("등록");
					JLabel clb1 = new JLabel("쿠폰코드");
					JLabel clb2 = new JLabel("상품명");
					JLabel clb3 = new JLabel("유효기간");
					JTextField ctf1 = new JTextField();
					Choice cc2 = new Choice();
					JTextField ctf3 = new JTextField();
					ArrayList<MenuBean> lists = mdao.selectAllMenu();
					for(int i=0; i<lists.size(); i++) {
						cc2.add(lists.get(i).getMenuName());
					}
					cInsert.setLayout(null);
					cInsert.add(clb1);
					cInsert.add(clb2);
					cInsert.add(clb3);
					cInsert.add(ctf1);
					cInsert.add(cc2);
					cInsert.add(ctf3);
					cInsert.add(cbtn);
					clb1.setBounds(20, 30, 50, 30);
					ctf1.setBounds(80, 30, 130, 30);
					clb2.setBounds(20, 70, 50, 30);
					cc2.setBounds(80, 70, 130, 30);
					clb3.setBounds(20, 110, 50, 30);
					ctf3.setBounds(80, 110, 130, 30);
					cbtn.setBounds(70, 200, 100, 40);
					cbtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String couponNum = ctf1.getText();
							String menuName = cc2.getSelectedItem();
							String couponExpirationDate = ctf3.getText();
							if(couponNum!=null && !couponNum.isEmpty()) {
								if(couponExpirationDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || couponExpirationDate.matches("^[0-9]{4}/[0-9]{2}/[0-9]{2}$")) {
									cdao.insert(couponNum, menuName, couponExpirationDate);
									refreshTable();
									cInsert.setVisible(false);
								} else {
									errorFrame("유효기간 다시입력해주세요",45,25,200,30);
								}
							} else {
								errorFrame("쿠폰코드 입력해주세요",50,25,200,30);
							}
						}
					});
					cInsert.setSize(250,300);
					cInsert.setLocationRelativeTo(null);
					cInsert.setVisible(true);
				}
				// 쿠폰관리 변경 OK
				else if(obj==updateBtn) {
					if(selectedRow >= 0) {
						JFrame cUpdate = new JFrame();
						JButton cbtn = new JButton("등록");
						JLabel clb1 = new JLabel("쿠폰번호");
						JLabel clb2 = new JLabel("상품명");
						JLabel clb3 = new JLabel("유효기간");
						JTextField ctf1 = new JTextField();
						ctf1.setEnabled(false);
						Choice cc2 = new Choice();
						JTextField ctf3 = new JTextField();
						ArrayList<MenuBean> lists = mdao.selectAllMenu();
						for(int i=0; i<lists.size(); i++) {
							cc2.add(lists.get(i).getMenuName());
						}
						cUpdate.setLayout(null);
						cUpdate.add(clb1);
						cUpdate.add(clb2);
						cUpdate.add(clb3);
						cUpdate.add(ctf1);
						cUpdate.add(cc2);
						cUpdate.add(ctf3);
						cUpdate.add(cbtn);
						cUpdate.setSize(250,300);
						cUpdate.setLocationRelativeTo(null);
						cUpdate.setVisible(true);
						clb1.setBounds(20, 30, 50, 30);
						ctf1.setBounds(80, 30, 130, 30);
						clb2.setBounds(20, 70, 50, 30);
						cc2.setBounds(80, 70, 130, 30);
						clb3.setBounds(20, 110, 50, 30);
						ctf3.setBounds(80, 110, 130, 30);
						cbtn.setBounds(70, 200, 100, 40);
						if(selectedRow >= 0) {
							updateBtn.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent e) {
									Object couponNum = table.getValueAt(selectedRow, 0);
									ctf1.setText(String.valueOf(couponNum));
									Object menuName = table.getValueAt(selectedRow, 1);
									cc2.select(String.valueOf(menuName));
									Object couponExpirationDate = table.getValueAt(selectedRow, 2);
									ctf3.setText(String.valueOf(couponExpirationDate));
								}
							});
							cbtn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									String couponNum = ctf1.getText();
									String menuName = cc2.getSelectedItem();
									String couponExpirationDate = ctf3.getText();
									if(couponExpirationDate.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || couponExpirationDate.matches("^[0-9]{4}/[0-9]{2}/[0-9]{2}$")) {
										cdao.update(couponNum, menuName, couponExpirationDate);
										refreshTable();
										cUpdate.setVisible(false);
									} else {
										errorFrame("유효기간 다시입력해주세요",45,25,200,30);
									}
								}
							});
						}
					} else {
						errorFrame("선택해주세요",80,25,200,30);
					}
				}
				// 쿠폰관리 삭제 OK
				else if(obj==deleteBtn) {
					if(selectedRow >= 0) { // 테이블에서 행이 선택되었는지 확인
						Object objNum = table.getValueAt(selectedRow, 0);
						cdao.delete(String.valueOf(objNum)); // 데이터베이스에서 데이터 삭제
						refreshTable();

					} else {
						errorFrame("선택해주세요",80,25,200,30);
					}
				}
			}
		}
	}
}
