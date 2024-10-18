import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class Menu implements ActionListener {
	
	MenuDao mdao = new MenuDao();
	OrderDao odao = new OrderDao();
	CouponDao cdao = new CouponDao();
	
	/* 메뉴화면 */
	JButton homeBtn = new JButton("돌아가기");
	JFrame menuFrame = new JFrame();
    JTabbedPane menuTabbedPane = new JTabbedPane();;
    String[] menuGroup = {"하우스스페셜","세트","단품","사이드","샐러드","음료"};
    Object[][] menuItems = null;
    Object[][] menuPrice = null;
    JButton menuButton = null;
    int i,j;
    Object[] columnNames = {"상품명","가격","수량","합계"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0); // DefaultTableModel 사용
    JTable orderTable = new JTable(tableModel); // 생성된 DefaultTableModel을 사용해서 JTable 생성
    JScrollPane orderScrollPane = new JScrollPane(orderTable);
    ArrayList<MenuBean> menuLists = null;
    int menuCount = 0;
    int totalPrice = 0;
    JPanel buyPanel = new JPanel();
    JButton mainBtn = new JButton("<html><center>메인화면</center><br><center>돌아가기</center></html>");
    JButton buyBtn = new JButton("주문하기");
    JButton couponBtn = new JButton("쿠폰사용");
    JLabel total = new JLabel("총금액 : " + totalPrice);
    JButton plusBtn = new JButton("+");
    JButton minusBtn = new JButton("-");   
    JButton delBtn = new JButton("X");   
    JButton allDelBtn = new JButton("전체취소");
    Font font = new Font("고딕",Font.BOLD,9);
    DefaultTableModel model;
    int row;
    
    public void menuFrame() {    	
        for(i=0; i<menuGroup.length; i++) {
            JPanel menuPanel = new JPanel(); // GridLayout 제거
            menuPanel.setLayout(new GridBagLayout()); // GridBagLayout 설정
            menuPanel.setBorder(new EmptyBorder(15, 13, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.NONE; // 가로로 채우지 않도록 수정
            gbc.anchor = GridBagConstraints.CENTER; // 컴포넌트를 셀의 중앙에 배치
            gbc.insets = new Insets(0, 0, 15, 15);
            
            /* menuGroup에 따라 menuItems 가져옴 */
            menuLists = mdao.getMenuItems(menuGroup[i]);
            menuItems = new Object[menuGroup.length][menuLists.size()];
        	menuPrice = new Object[menuGroup.length][menuLists.size()];
            for(j=0; j<menuLists.size(); j++) {
            	menuItems[i][j] = menuLists.get(j).getMenuName();
            	menuPrice[i][j] = menuLists.get(j).getMenuPrice();
            }

            /* button에 이미지+상품명+가격 배치 */
            for(j=0; j<menuItems[i].length; j++) {
            	final Object name = menuItems[i][j]; // final 변수 생성
            	final Object price = menuPrice[i][j];
                menuButton = new JButton("<html><center>"+name.toString()
                                            +"</center><br><center>가격 : "+menuPrice[i][j]
                                            +"</center></html>");
            	menuButton.setPreferredSize(new Dimension(170, 170));
            	menuButton.setBackground(Color.white);
            	gbc.gridx = j % 3;
            	gbc.gridy = j / 3;
            	gbc.gridwidth = 1;
            	menuPanel.add(menuButton, gbc);
            	try {
            		Image img = ImageIO.read(new File("images/"+menuItems[i][j]+".jpg"));
            		Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            		menuButton.setIcon(new ImageIcon(resizedImg));
            	} catch (Exception e) {
            		System.out.println(menuItems[i][j] + " 사진등록필요");
            	}
            	menuButton.setHorizontalTextPosition(JButton.CENTER);
            	menuButton.setVerticalTextPosition(JButton.BOTTOM);
            	/* 주문할 상품목록 */
            	menuButton.addActionListener(new ActionListener() {
            	    public void actionPerformed(ActionEvent e) {
            	        System.out.println(name+" 버튼눌림");
            	        model = (DefaultTableModel) orderTable.getModel();
            	        int rowIndex = -1;
            	        for (int i = 0; i < model.getRowCount(); i++) {
            	            if (model.getValueAt(i, 0).equals(name)) { // 첫번째열 : "상품명"
            	                rowIndex = i;
            	                break;
            	            }
            	        }
            	        if (rowIndex == -1) {
            	            model.addRow(new Object[]{name, price, 1, price}); // 새로운상품(수량 1)
            	            totalPrice += (int)price;
            	            System.out.println(totalPrice);
            	        } else {
            	            int quantity = (int)model.getValueAt(rowIndex, 2); // 세번째열 : "수량"
            	            model.setValueAt((quantity+1), rowIndex, 2); // 기존상품(수량 +1)
            	            model.setValueAt(((int)price*(quantity+1)), rowIndex, 3); // 상품가격*수량
            	            totalPrice += (int)price;
            	            System.out.println(totalPrice);
            	        }
            	        total.setText("총금액 : " + totalPrice); // totalPrice 값이 변경될 때마다 total JLabel 업데이트
            	    }
            	});
            }
            menuFrame.setLayout(null);
            JScrollPane menuScrollPane = new JScrollPane(menuPanel);
            menuScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 수평 스크롤바가 나타나지 않도록 설정
            menuScrollPane.getVerticalScrollBar().setUnitIncrement(20); // 스크롤바 속도조정
            menuTabbedPane.addTab(menuGroup[i], menuScrollPane);
            menuTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    // 탭이 변경될 때마다, 현재 선택된 탭의 스크롤바를 맨 위로 이동
                    JScrollPane currentScrollPane = (JScrollPane) menuTabbedPane.getSelectedComponent();
                    currentScrollPane.getVerticalScrollBar().setValue(0);
                }
            });
        }
        buyPanel.setLayout(null);
        buyPanel.add(buyBtn);
        buyPanel.add(couponBtn);
        buyPanel.add(total);
        buyPanel.add(plusBtn);
        buyPanel.add(minusBtn);
        buyPanel.add(delBtn);
        buyPanel.add(allDelBtn);
        buyPanel.add(mainBtn);
        buyBtn.setBounds(300,55,140,50);
        couponBtn.setBounds(440,55,140,50);
        total.setBounds(175,55,150,50);
        plusBtn.setBounds(360, 5, 40, 40);
        minusBtn.setBounds(400, 5, 40, 40);
        delBtn.setBounds(440, 5, 40, 40);
        allDelBtn.setBounds(480,5,100,40);
        mainBtn.setBounds(5, 5, 100, 100);
        plusBtn.setFont(font);
        minusBtn.setFont(font);
        delBtn.setFont(font);
        allDelBtn.setFont(font);
        total.setFont(new Font("고딕",Font.BOLD,15));
        plusBtn.setContentAreaFilled(false);
        minusBtn.setContentAreaFilled(false);
        delBtn.setContentAreaFilled(false);
        allDelBtn.setContentAreaFilled(false);
        buyPanel.setBounds(0,650,580,150);
        buyBtn.addActionListener(this);
        couponBtn.addActionListener(this);
        plusBtn.addActionListener(this);
        minusBtn.addActionListener(this);
        delBtn.addActionListener(this);
        allDelBtn.addActionListener(this);
        mainBtn.addActionListener(this);
   		orderTable.addMouseListener(new MouseHandler());
        menuFrame.add(buyPanel);
        menuFrame.add(menuTabbedPane);
        menuTabbedPane.setBounds(0,0,590,500);
        menuFrame.add(orderScrollPane);
        orderScrollPane.setBounds(0,500,590,150);
        menuFrame.add(homeBtn);
        homeBtn.setBounds(0, 500, 100, 100);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setTitle("키오스크");
        menuFrame.setSize(600, 800);
        menuFrame.setLocationRelativeTo(null); // 화면 중앙에 표시
        menuFrame.setResizable(false);
        menuFrame.setBackground(Color.ORANGE);
        menuFrame.setVisible(true);
    }
    
    /* 쿠폰 사용시 */
    JFrame couponFrame = new JFrame();
    JLabel cpLabel = new JLabel("쿠폰입력 : ");
    JTextField cpInput = new JTextField();
	private void coupon() {
		couponFrame.add(cpLabel);
		couponFrame.add(cpInput);
		couponFrame.setLayout(null);
		cpLabel.setBounds(40, 5, 100, 50);
		cpInput.setBounds(100,15,150,30);
		cpInput.addActionListener(this);
		couponFrame.setSize(300, 100);
		couponFrame.setLocationRelativeTo(null);
		couponFrame.setVisible(true);
	}
	
	/* 주문 성공시 */
	private void success(int orderNum) {
		System.out.println("주문성공");
		JFrame sucFrame = new JFrame();
		JLabel sucLabel = new JLabel("주문번호");
		JLabel numLabel = new JLabel(String.valueOf(orderNum));
		JButton okBtn = new JButton("확인");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sucFrame.dispose(); // 창 닫기
				menuFrame.setVisible(false);
				new Main("키오스크").showMainScreen();
			}
		});
		sucFrame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	menuFrame.setVisible(false);
		        new Main("키오스크").setVisible(true);
		    }
		});
		sucFrame.add(sucLabel);
		sucFrame.add(numLabel);
		sucFrame.add(okBtn);
		sucLabel.setBounds(115, 30, 80, 30);
		numLabel.setBounds(133, 60, 50, 30);
		okBtn.setBounds(100, 100, 80, 30);
		sucFrame.setLayout(null);
		sucFrame.setSize(300,200);
		sucFrame.setLocationRelativeTo(null);
		sucFrame.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==cpInput) {
			String couponNum = cpInput.getText();
			int result = cdao.getCouponNum(couponNum);
			cpInput.setText("");
			if(result==1) {
				System.out.println("쿠폰 사용");
				// JTable에 쿠폰내용 가져와서 추가하는코드
			} else {
				JFrame noF = new JFrame();
				JLabel noL = new JLabel("유효하지 않은 쿠폰입니다");
				noF.add(noL);
				noF.setLayout(null);
				noL.setBounds(23, 7, 200, 50);
				noF.setSize(200, 100);
				noF.setLocationRelativeTo(null);
				couponFrame.setVisible(false);
				noF.setVisible(true);
			}
		}
		if(obj==mainBtn) {
			menuFrame.setVisible(false);
			new Main("키오스크").showMainScreen();
		}
		if(obj==buyBtn) { // 주문번호 받는곳
			DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
			if(model.getRowCount() > 0) {
				System.out.println("주문하기");
				int orderNum = odao.orderNum()+1;
				for (int i = 0; i < model.getRowCount(); i++) {
					String orderMenu = (String) model.getValueAt(i, 0); // 상품명
					int orderCount = (Integer) model.getValueAt(i, 2); // 수량
					int price = (Integer) model.getValueAt(i, 3); // 합계 (가격*수량)
					odao.insert(orderNum,orderMenu,orderCount,price); // 각 행의 데이터를 orderMenu 테이블에 삽입
				}
				success(orderNum);
			} else {
				JFrame needF = new JFrame();
				JLabel needL = new JLabel("상품을 먼저 담아주세요");
				needF.add(needL);
				needF.setLayout(null);
				needL.setBounds(27, 7, 200, 50);
				needF.setSize(200, 100);
				needF.setLocationRelativeTo(null);
				needF.setVisible(true);
			}
		}
		if(obj==couponBtn) {
			System.out.println("쿠폰사용");
			coupon();
		}
		int row = orderTable.getSelectedRow(); // 현재 선택된 행의 인덱스를 가져옴
		if (obj == plusBtn || obj == minusBtn || obj == delBtn || obj == allDelBtn) {
			try {
				if (model.getRowCount() > 0 && row != -1) { // JTable에 행이 존재하고, 유효한 행이 선택되었는지 확인
					int price = (int) model.getValueAt(row, 1); // 선택된 행의 가격
					int quantity = (int) model.getValueAt(row, 2); // 선택된 행의 수량
					if (obj == plusBtn) {
						model.setValueAt((quantity + 1), row, 2); // 수량 +1
						model.setValueAt(((int) price * (quantity + 1)), row, 3); // 상품가격*수량
						totalPrice += price; // 가격 추가
					} else if (obj == minusBtn) {
						if (quantity > 1) {
							model.setValueAt((quantity - 1), row, 2); // 수량 -1
							model.setValueAt(((int) price * (quantity - 1)), row, 3); // 상품가격*수량
							totalPrice -= price; // 가격 감소
						}
					} else if (obj == delBtn) {
						totalPrice -= price * quantity; // 가격 * 수량 감소
						model.removeRow(row); // 행 삭제
					}
				}
				if (obj == allDelBtn) {
					if (model.getRowCount() > 0) { // JTable에 한 행이라도 존재하면
						totalPrice = 0;
						model.setRowCount(0); // 모든 행 삭제
					}
				}
				total.setText("총금액 : " + totalPrice); // totalPrice 값이 변경될 때마다 total JLabel 업데이트
			} catch(NullPointerException ne) {
			}
		}
	}

	/* Table 관련 작업 */	
	class MouseHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			row = orderTable.getSelectedRow(); // 선택된 테이블 행번호
		}
	}
}
