package chessGame;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Lobby extends JFrame{
	public Lobby(){
		setTitle("버튼만들기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 300);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		
		JButton b1 = new JButton("2인 게임 백");
		b1.setBounds(182, 133, 124, 15);
		b1.setBackground(Color.white);
		setLocationRelativeTo(null);
		JButton b2 = new JButton("2인 게임 흑");
		b2.setBounds(182, 200, 124, 15);
		b2.setBackground(Color.white);
		JButton btn = new JButton("1인 게임");
		btn.setBounds(182,59,119,23);
		btn.setBackground(Color.white);
		contentPane.add(btn);
		contentPane.add(b1);
		contentPane.add(b2);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ChessSwingforOne();
			}
			
		});
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				try {
					new ChessSwingServer(); //서버의 비즈니스 로직이 들어간 함수를 실행한다.
					
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
			
		});
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				try {
					new ChessSwing();
				} catch (IOException ie) {
					ie.printStackTrace();
				} 
			}
			
		});
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Lobby();
	}
}
