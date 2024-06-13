package chessGame;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessSwingforOne extends JFrame{
	private Image bufferImage;
	private Graphics screenGraphic; // repaint()할때 화면 무한깜빡 없애기 위한 변수
	ChessBoard chess = new ChessBoard(); // 체스보드 생성
	int turn = 1; // 홀수일때 백색의 턴
	int clicktimes = 0; // 몇번째 클릭인지에 따라 다른 함수 처리하기 위함
	int promoX = 0;// 프로모션할때  좌표 저장용 변수
	boolean checked = false; // 흑색이 체크상태인지
	boolean wchecked = false; // 백색이 체크상태인지
	boolean Bpromotion = false; // 흑색이 프로모션 할수있는지
	boolean Wpromotion = false; // 백색이 프로모션 할 수 있는지
	boolean cango[][] = new boolean[8][8]; // 그 자리에 이동할 수 있는지 boolean array
	private Image plate = new ImageIcon("./ImageForChess/Chessboard.jpg").getImage();
	private Image pawn = new ImageIcon("./ImageForChess/pawn.png").getImage();
	private Image king = new ImageIcon("./ImageForChess/king.png").getImage();
	private Image checkedwking = new ImageIcon("./ImageForChess/checkedwKing.png").getImage();
	private Image checkedking = new ImageIcon("./ImageForChess/checkedKing.png").getImage();
	private Image queen = new ImageIcon("./ImageForChess/queen.png").getImage();
	private Image knight = new ImageIcon("./ImageForChess/knight.png").getImage();
	private Image bishop = new ImageIcon("./ImageForChess/bishop.png").getImage();
	private Image rook = new ImageIcon("./ImageForChess/rook.png").getImage();
	private Image Wpawn = new ImageIcon("./ImageForChess/WhitePawn.png").getImage();
	private Image Wking = new ImageIcon("./ImageForChess/WhiteKing.png").getImage();
	private Image Wqueen = new ImageIcon("./ImageForChess/WhiteQueen.png").getImage();
	private Image Wknight = new ImageIcon("./ImageForChess/WhiteKnight.png").getImage();
	private Image Wbishop = new ImageIcon("./ImageForChess/WhiteBishop.png").getImage();
	private Image Wrook = new ImageIcon("./ImageForChess/WhiteRook.png").getImage();
	private Image star = new ImageIcon("./ImageForChess/star.jpg").getImage();
	// 판,말, 이동가능좌표 등에 사용되는 아이콘들
	
	public ChessSwingforOne(){
		JPanel panel = new JPanel(); 
		setContentPane(panel);
		panel.addMouseListener(new MymouseListener()); // 프레임 속 패널에 마우스리스너, 내가 만든 함수 설정
		setTitle("Chess"); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x눌렀을때 프로그램 제대로 꺼지도록
		setSize(400, 450); // 크기 조정
		setLocationRelativeTo(null); // 화면 가운데 뜨도록 설정 
		setVisible(true);
		setResizable(false); // 크기 고정
		
	}
	
	class MymouseListener implements MouseListener {
		int inputX = 0;
		int inputXtogo = 0;
		int inputY = 0;
		int inputYtogo = 0;
		int umj = 0; // 움직인 횟수
		boolean checkmate = false;
		Piece enp = new Piece(10,10);
		@Override
		public void mouseClicked(MouseEvent e) {
			if(checkmate) System.exit(0);
			umj = 0;
			checked = false;
			wchecked = false;
			if(clicktimes == 0) { // 처음 클릭했을때
			inputX = (e.getY()-20)/45; // 들어온 좌표를 Chess에 맞게 변환
			inputY = (e.getX()-20)/45;
			System.out.println(inputX+", "+ inputY);
			umj = 0;
			if(chess.board[inputX][inputY] == null) {
				JOptionPane.showInternalMessageDialog(null, "빈 칸입니다.");
				System.out.println("빈 칸입니다.");
			}else if(turn%2 == 1 && chess.board[inputX][inputY].color != 'W') {
				JOptionPane.showInternalMessageDialog(null, "백색의 턴입니다.");
				System.out.println("백색의 턴입니다!");
			}else if(turn%2 == 0 &&chess.board[inputX][inputY].color != 'B') {
				JOptionPane.showInternalMessageDialog(null, "흑색의 턴입니다.");
				System.out.println("흑색의 턴입니다!");
			}else if(inputX>7 || inputY<0 || inputY >7 || inputX<0) {
				JOptionPane.showInternalMessageDialog(null, "잘못된 입력입니다.");
				System.out.println("잘못된 입력입니다");
			}else if(chess.board[inputX][inputY] != null) { // 찍은 것을 움직일 수 있고 빈 칸이 아닐 때
				for(int i = 0; i<8; i++) {
					for(int j = 0; j<8; j++) {
						try{
						cango[i][j] = false;
						if(chess.board[inputX][inputY].canmove(i, j,chess)) { // 움직일 수 있을 경우
							cango[i][j] = true;	// 움직일 수 있다 선언
							umj++; // 움직일수있는곳 개수 +1
							if(cango[i][j]) {// 근데
								Piece p = new Piece(9,9); // 임의로 가려는 곳에 있는 자리에 피스가 있으면 여기 저장해두고
								p = chess.board[i][j];
								chess.board[inputX][inputY].move(i, j, chess); // 거기 직접 가봤더니
								if(turn %2 == 1){ // 내가 체크 당하고 있으면
									wchecked = Common.checked(chess, 'W'); 
									if(wchecked) {cango[i][j] = false; umj --;} // 거긴 못움직여
								}else if(turn%2 == 0){
									checked = Common.checked(chess, 'B');
									if(checked) {cango[i][j] = false; umj --;}
								}
								chess.board[i][j].move(inputX, inputY, chess);
								chess.board[i][j] = p;
								chess.board[inputX][inputY].movetimes -= 2;// 원상복구
							}
							
						}
						}catch(NullPointerException ne) {continue;}// 가려던곳 빈칸이면 넘어가라
					}
				}
				try {
				if(turn %2== 1 && Math.abs(inputY-inputYtogo)==1&& chess.board[inputX][inputY].type == 'P' &&chess.board[inputXtogo][inputYtogo].type == 'P' &&chess.board[inputXtogo][inputYtogo].movetimes == 1&&inputXtogo == 3
						&& inputX ==inputXtogo) {
					cango[inputX-1][inputYtogo] = true; 
				}
					
				else if(turn %2== 0&& Math.abs(inputY-inputYtogo)==1 && chess.board[inputX][inputY].type == 'P'&& chess.board[inputXtogo][inputYtogo].type == 'P'&&chess.board[inputXtogo][inputYtogo].movetimes == 1&&inputXtogo == 4 
						&& inputX == inputXtogo) {
					cango[inputX+1][inputYtogo] = true;
				}
				} catch(NullPointerException ne) {} // 앙파상
				if(umj == 0) { //움직일수있는곳 개수가 0이면 못움직입니다.
					JOptionPane.showInternalMessageDialog(null, "그 말은 움직일 수 없습니다.");
					System.out.println("그 말은 움직일 수 없습니다.");
				}else { // 움직일수 있는 곳의 개수가 0만 아니라면 다음으로 넘어갑니다.
					clicktimes ++;
				}
			}
			
			}else if(clicktimes == 1) {
				inputXtogo = (e.getY()-20)/45;
				inputYtogo = (e.getX()-20)/45; 
				if(cango[inputXtogo][inputYtogo]) {// 새로 입력받은 좌표가 아까 구해둔 움직일수 있는 데이터상으로 움직일 수 있을때
					enp = chess.board[inputXtogo][inputYtogo];
					chess.board[inputX][inputY].move(inputXtogo, inputYtogo, chess); // 움직인다.
					turn++;// 다음턴으로 넘어간다.
					if(chess.board[inputXtogo][inputYtogo].type == 'K' && Math.abs(inputYtogo-inputY) ==2) {
						try {// 만약 왕이 두칸을 움직였다면(캐슬링)
						int direc = (inputYtogo-inputY)/2; 
						if(inputYtogo>inputY) {
							chess.board[inputXtogo][inputYtogo+direc].move(inputXtogo, inputYtogo-direc, chess);
						}
						else {
							chess.board[inputXtogo][inputYtogo+2*direc].move(inputXtogo, inputYtogo-direc, chess);
						}// 룩이 같이 움직인다.
						
						}catch (NullPointerException ne) {}
							
					}
					try {// 옆으로 움직였는데 원래 있던 자리가 비어있으면 잡아먹은 취급
						if(chess.board[inputXtogo][inputYtogo].type == 'P' && Math.abs(inputYtogo-inputY) == 1 && enp == null) {
							if(chess.board[inputXtogo][inputYtogo].color == 'W') {
								chess.board[inputXtogo+1][inputYtogo] = null;
							}
							else
								chess.board[inputXtogo-1][inputYtogo] = null;
						}
					}catch (NullPointerException ne) {}
					
				}else { // 만약 움직일 수 없는 곳 좌표가 들어왔다면
					JOptionPane.showInternalMessageDialog(null, "그 곳으로는 이동할 수 없습니다.");
					System.out.println("그 곳으로는 이동할 수 없습니다.");
				}// 못움직임
				
				
				for(int i = 0; i<8; i++) {
					for(int j = 0; j<8; j++) {
						cango[i][j] = false;
					}
				}// 더이상 움직일수있는자리에 별이 안떠도 좋다
				try {
				if(chess.board[inputXtogo][inputYtogo].type == 'P' ) {// 폰이
					if(chess.board[inputXtogo][inputYtogo].color == 'B') {// 끝까지 갔다면
						if(inputXtogo == 7) {
							Bpromotion = true;// 프로모션이 가능하다
							clicktimes = 2; // 예외적으로 클릭타임즈 2로 가고
							promoX = inputYtogo; // 데이터 저장해두고
						}
					}else if(chess.board[inputXtogo][inputYtogo].color == 'W') {
						if(inputXtogo == 0) {
							Wpromotion = true;
							clicktimes = 2;
							promoX = inputYtogo;
						}
					}
				}
				}catch(NullPointerException ne) {}
				if(Bpromotion == false &&  Wpromotion == false) {
					clicktimes = 0;//프로모션이 안될경우 처음으로 돌아간다.
				}
				
			}else if(clicktimes == 2) {//프로모션 한다
				int promoInputX = (e.getY()-20)/45;
				int promoInputY = (e.getX()-20)/45;
				if(chess.board[inputXtogo][inputYtogo].color == 'W') {// 만약 백색말이라면
				if(promoInputY == inputYtogo) {
					switch(promoInputX) { // 뭘 클릭했느냐에 따라 프로모션을 한다.
					case 0 : chess.board[inputXtogo][inputYtogo] = new Queen(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'W';
						break;
					case 1 : chess.board[inputXtogo][inputYtogo] = new Rook(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'W';
						break;
					case 2 : chess.board[inputXtogo][inputYtogo] = new Bishop(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'W';
						break;
					case 3 :	chess.board[inputXtogo][inputYtogo] = new Knight(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'W';
						break;
					}clicktimes = 0;
					Wpromotion = false; // 프로모션 끝
				
				}else {
					JOptionPane.showInternalMessageDialog(null, "잘못된 프로모션입니다.");
					System.out.println("잘못된 좌표입니다.");
				}
				}else {
				if(promoInputY == inputYtogo) {
					switch(promoInputX) {
					case 7 : chess.board[inputXtogo][inputYtogo] = new Queen(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'B';
						break;
					case 6 : chess.board[inputXtogo][inputYtogo] = new Rook(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'B';
						break;
					case 5 : chess.board[inputXtogo][inputYtogo] = new Bishop(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'B';
						break;
					case 4 :	chess.board[inputXtogo][inputYtogo] = new Knight(inputXtogo, inputYtogo, chess);
						chess.board[inputXtogo][inputYtogo].color = 'B';
						break;
					}clicktimes = 0;
					Bpromotion = false;
				
				}else {
					JOptionPane.showInternalMessageDialog(null, "잘못된 프로모션입니다.");
					System.out.println("잘못된 좌표입니다");
				}
				}
				
			}
			if(turn %2 == 0) { // 흑이 체크 당하고있다면
			outer : // 체크메이트 테스트
						for(int k = 0; k<8; k++) {
							for(int l = 0; l<8; l++) {
								for(int i = 0; i<8; i++) {
									for(int j = 0; j<8; j++) {
										try{
											if(chess.board[k][l].color == 'B' && chess.board[k][l].canmove(i, j,chess)) { // 입력받은 x y 좌표에서 이동할 수 있는지
												umj++; // 모든 좌표를 확인해서
												if(chess.board[k][l].canmove(i, j,chess)) { // 모든 좌표 중 이동할수있는곳이 있는지 체크
													Piece p = new Piece(9,9);
													p = chess.board[i][j];
													chess.board[k][l].move(i, j, chess);
													checked = Common.checked(chess, 'B');
													if(checked) {umj --;}
													chess.board[i][j].move(k, l, chess);
													chess.board[i][j] = p;
													chess.board[k][l].movetimes -= 2;
													if(umj !=0) break outer;// 하나라도 이동할수있으면 포문 그만.
												}
											}
									}catch(NullPointerException ne) {continue;}
							}
							
						}
					}
				}
			}
			else {
			outer :
			for(int k = 0; k<8; k++) {
				for(int l = 0; l<8; l++) {
					for(int i = 0; i<8; i++) {
						for(int j = 0; j<8; j++) {
							try{
								if(chess.board[k][l].color == 'W' && chess.board[k][l].canmove(i, j,chess)) { // 입력받은 x y 좌표에서 이동할 수 있는지
									umj++; // 모든 좌표를 확인해서
									if(chess.board[k][l].canmove(i, j,chess)) {
										Piece p = new Piece(9,9);
										p = chess.board[i][j];
										chess.board[k][l].move(i, j, chess);
										wchecked = Common.checked(chess, 'W');
										if(wchecked) {umj --;}
										chess.board[i][j].move(k, l, chess);
										chess.board[i][j] = p;
										chess.board[k][l].movetimes -= 2;
										if(umj !=0) break outer;
									}
								}
						}catch(NullPointerException ne) {continue;}
				}
				
			}
		}
	}
			}
			for(int i = 0; i<8; i++) {
				for(int j = 0; j<8; j++) {
					checked = Common.checked(chess, 'B');
					wchecked = Common.checked(chess, 'W');
				}
			} // 체크 상태 움직임 테스트 전으로 복구.
				if(umj == 0 && checked)  // 못움직이는데 흑이 체크당하면 백 승
				{System.out.println("체크메이트");
				JOptionPane.showInternalMessageDialog(null, "Checkmate!!Winner is white");
				checkmate = true;
//				System.exit(0);
				}else if(umj == 0 && wchecked){System.out.println("체크메이트"); // 백이 체크당하면 흑승
				JOptionPane.showInternalMessageDialog(null, "Checkmate!!Winner is black");
				checkmate = true;
	//			System.exit(0);
				}else if(umj == 0) { // 둘다아니면 무승부
					JOptionPane.showInternalMessageDialog(null, "draw");
					checkmate = true;
				}
					
				wchecked = Common.checked(chess, 'W');
				checked = Common.checked(chess, 'B'); // 체크상태에따라 왕 모양 바까줌
		
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void paint(Graphics g) { // 기본 배경
		bufferImage = createImage(400, 450);
		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}
	public void screenDraw(Graphics g) { // 매번 새로 출력
		g.drawImage(plate, 25, 50, null );
		for(int i = 0; i<8; i++) { // 판 전체를 출력하는데, 피스의 타입에 따라 다른 애를 출력
			for(int j = 0; j<8; j++) {
				try {
				if(chess.board[i][j].color == 'B') {
					switch(chess.board[i][j].type) {
					case 'R': g.drawImage(rook, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
					case 'Q': g.drawImage(queen, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
					case 'K': {
								if(checked) {
									g.drawImage(checkedking, chess.board[i][j].swingx, chess.board[i][j].swingy, null);
								}else {
									g.drawImage(king, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
								}break;
							}
					case 'N': g.drawImage(knight, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
					case 'B': g.drawImage(bishop, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
					case 'P': g.drawImage(pawn, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
					}
					
				}else if(chess.board[i][j].color == 'W') {
				switch(chess.board[i][j].type) {
				case 'R': g.drawImage(Wrook, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
				case 'Q': g.drawImage(Wqueen, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
				case 'K': {
						if(wchecked) {
							g.drawImage(checkedwking, chess.board[i][j].swingx, chess.board[i][j].swingy, null);
						}else {
							g.drawImage(Wking, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
						}break;
				}
				case 'N': g.drawImage(Wknight, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
				case 'B': g.drawImage(Wbishop, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
				case 'P': g.drawImage(Wpawn, chess.board[i][j].swingx, chess.board[i][j].swingy, null); break;
				}
				
				}
			}catch (NullPointerException ne) {}
		}	
		}
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j< 8; j++) {
				if(cango[j][i]) {
					g.drawImage(star, i*45+30, j*45+55, null);
				}
			} // 클릭된 자리의 말이 움직일 수 있는곳에 별 출력
		}
		if(Bpromotion) { // 프로모션 가능해졌을때 어디를 클릭했을때 뭘로 프로모션하는지
			g.drawImage(queen, promoX*45+30, 7*45 +55, null);
			g.drawImage(rook, promoX*45+30, 6*45 +55, null);
			g.drawImage(bishop, promoX*45+30, 5*45 +55, null);
			g.drawImage(knight, promoX*45+30, 4*45 +55, null);
		}else if(Wpromotion){
			g.drawImage(Wqueen, promoX*45+30, 0*45 +55,  null);
			g.drawImage(Wrook, promoX*45+30,1*45 +55,  null);
			g.drawImage(Wbishop, promoX*45+30, 2*45 +55,  null);
			g.drawImage(Wknight, promoX*45+30, 3*45 +55,  null);
		}
		
		this.repaint();
	}
	
	public static void main(String[] args) {
		new ChessSwingforOne();
	}
}
