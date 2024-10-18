import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class Surve implements ActionListener {
	

	JPanel pSurve = new JPanel();
	JButton inBtn = new JButton("매장");
	JButton outBtn = new JButton("포장");
	JFrame surveFrame = new JFrame();
	Container surveContentPane = surveFrame.getContentPane();
	// 테두리 여백 및 색상 지정
	Border padding = BorderFactory.createEmptyBorder(30, 30, 30, 30);
	Border border = BorderFactory.createMatteBorder(30, 30, 30, 30, Color.ORANGE);
	CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(border, padding);
	
	public void surveFrame() {
		/* 3.서브화면 "매장"버튼 */
		try {
			Image img = ImageIO.read(new File("images/inBtn.jpg"));
			Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		    inBtn.setIcon(new ImageIcon(resizedImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		inBtn.setHorizontalTextPosition(JButton.CENTER);
		inBtn.setVerticalTextPosition(JButton.BOTTOM);
		pSurve.setLayout(null);
		inBtn.setBounds(20,200,200,200);
		pSurve.add(inBtn);
		inBtn.addActionListener(this);
		surveContentPane.add(pSurve);
		/* 4.서브화면 "포장"버튼 */
		try {
			Image img = ImageIO.read(new File("images/outBtn.jpg"));
			Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		    outBtn.setIcon(new ImageIcon(resizedImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		outBtn.setHorizontalTextPosition(JButton.CENTER);
		outBtn.setVerticalTextPosition(JButton.BOTTOM);
		pSurve.setLayout(null);
		outBtn.setBounds(250,200,200,200);
		pSurve.add(outBtn);
		outBtn.addActionListener(this);
		surveContentPane.add(pSurve);
		surveFrame.getRootPane().setBorder(compoundBorder);
		surveFrame.setTitle("키오스크");
		surveFrame.setSize(600,800);
		surveFrame.setLocationRelativeTo(null); // 화면 중앙에 표시
		surveFrame.setResizable(false);
		surveFrame.setVisible(true);
	}
	
	String surve = null;
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==inBtn || obj==outBtn) {
			if(obj==inBtn)
				surve = "매장";
			else if(obj==outBtn)
				surve = "포장";
			new Menu().menuFrame();
			surveFrame.setVisible(false);
		}
	}
}
