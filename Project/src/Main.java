import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;


/* 
 * 1. 쿠폰사용
 * 2. 매장/포장 -> 주문관리에서 확인
 */


public class Main extends JFrame implements ActionListener {
	
	// 테두리 여백 및 색상 지정
	Border padding = BorderFactory.createEmptyBorder(30, 30, 30, 30);
	Border border = BorderFactory.createMatteBorder(30, 30, 30, 30, Color.ORANGE);
	CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(border, padding);
	
/* 메인화면(테스트용) */
	Container mainContentPane = getContentPane(); // 컨테이너 : 프레임의 모든 컴포넌트가 추가되는 공간
	JPanel pMain = new JPanel();
	JButton mainBtn = new JButton("주문하기");
	JButton adminBtn = new JButton("");
	
	Main(String title){

		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE); // X버튼 누르면 프로그램 종료!
		
		/* 1.메인화면 이미지 */
		File mainImageFile = new File("images/burger.jpg"); // 해당 경로의 이미지 파일을 참조하기위한 File 객체
		BufferedImage mainImage = null; // 이미지 파일을 읽어서 저장할 변수
		try {
			mainImage = ImageIO.read(mainImageFile); // 이미지 파일 읽어옴
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image scaledImage = mainImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // SMOOTH : 고품질처리
		JLabel mainLabel = new JLabel(new ImageIcon(scaledImage)); // 크기 조절된 이미지를 Icon으로 만든 후 이를 사용해 레이블생성
		mainContentPane.setLayout(null); // 내마음대로 배치하기 위해 배치관리자 null로 설정
		mainLabel.setBounds(90,100,300,300);
		mainContentPane.add(mainLabel); // 컨테이너에 레이블 올리기
		
		/* 2.메인화면 "주문하기"버튼 */
		pMain.setLayout(null); // 내마음대로 배치하기 위해 배치관리자 null로 설정
		pMain.setBounds(0,0,600,800); // 버튼패널 위치/크기 설정
		mainBtn.setBounds(160,430,150,50); // 버튼 위치/크기 설정
		adminBtn.setBounds(100,0,270,80);
		pMain.add(mainBtn);
		pMain.add(adminBtn);
		adminBtn.setBorderPainted(false);
		adminBtn.setContentAreaFilled(false);
		mainBtn.addActionListener(this);
		adminBtn.addActionListener(this);
		mainContentPane.add(pMain);
		
		setSize(600,800); // 창 사이즈조정
		setLocationRelativeTo(null); // 창 위치조정(화면 중앙에 표시)
		setResizable(false); // 창 크기고정
		getRootPane().setBorder(compoundBorder);
		setVisible(true); // 창 보이게하기
	}
    
	/* Btn 클릭 이벤트 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == mainBtn) {
			new Surve().surveFrame();
			super.setVisible(false);
		}
		if(obj == adminBtn) {
			System.out.println("관리자모드");
			new Admin().adminLogin();
		}
	}
	
	public void showMainScreen() {
		this.setVisible(true);
	}
	
/* 메인함수 */
	public static void main(String[] args) {
		new Main("키오스크");
	}

}
