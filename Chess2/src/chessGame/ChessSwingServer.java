package chessGame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class ChessSwingServer extends JFrame{
	char[] sendtoserver = new char[6];
	boolean error = false;
	char[] getmove = new char[6];
	public void closeAll(ServerSocket serverSocket, Socket socket, BufferedReader br, PrintWriter pw)
			throws IOException {
		if (pw != null)
			pw.close();
		if (br != null)
			br.close();
		if(socket!=null)
			socket.close();
		if (serverSocket != null) {
			serverSocket.close();
			System.out.println("서버종료");
		}
	}
	int getX=0, getY=0, getXtogo=0, getYtogo=0;
	boolean checkmate = false;
	String Fromclient = "";
	ServerSocket serverSocket = null;  //클라이언트의 요청을 받아들일 Server가 될 소켓
	Socket socket = null;  //요청을 보낸 클라이언트마다 연결을 위해 생성하는 소켓
	BufferedReader br=null;
	PrintWriter pw = null;
	private Image bufferImage;
	private Graphics screenGraphic; // repaint()할때 화면 무한깜빡 없애기 위한 변수
	ChessBoard chess = new ChessBoard(); // 체스보드 생성
	int turn = 1; // 홀수일때 백색의 턴
	int clicktimes = 0; // 몇번째 클릭인지에 따라 다른 함수 처리하기 위함
	int promoX = 0;// 프로모션할때  좌표 저장용 변수
	boolean checked = false; // 흑색이 체크상태인지
	boolean wchecked = false; // 백색이 체크상태인지
	boolean promotion = false; // 백색이 프로모션 할 수 있는지
	boolean cango[][] = new boolean[8][8]; // 그 자리에 이동할 수 있는지 boolean array
	String gamelog = "";
	Piece[] deadPiece = new Piece[32];
	int countdeadPiece = 0;
	int promotionCount = 0; 
	int[] promotionMoveTimes = new int[16];
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
	
	public ChessSwingServer() throws UnknownHostException, IOException{
		JPanel panel = new JPanel(); 
		setContentPane(panel);
		
		panel.addMouseListener(new MymouseListener()); // 프레임 속 패널에 마우스리스너, 내가 만든 함수 설정
		setTitle("Chess_Server"); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x눌렀을때 프로그램 제대로 꺼지도록
		setSize(400, 450); // 크기 조정
		setLocationRelativeTo(null); // 화면 가운데 뜨도록 설정 
		setVisible(true);
		setResizable(false); // 크기 고정
		try {
			serverSocket = new ServerSocket(8080); //port번호를 5432로 설정하여 서버소켓 생성
			System.out.println("서버시작");
			System.out.println(getIp());
			socket = serverSocket.accept(); // 클라이언트의 요청을 받아들여 연결을 유지할 소켓을 반환받아 저장
			System.out.println("소켓생성");
			Scanner sc = new Scanner(System.in);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  //소켓의 입력 스트림을 2byte ProcessStream에 저장
			pw = new PrintWriter(socket.getOutputStream(), true); //소켓의 출력 스트림을 2byte ProcessStream에 저장
			while(true) {
				Fromclient = br.readLine();
				
				if(Fromclient.charAt(0) != 'U') {
				turn ++;
				try {
					getmove = Fromclient.toCharArray();
					getX = getmove[0]-'0';
					getY = getmove[1]-'0';
					getXtogo = getmove[2]-'0';
					getYtogo = getmove[3]-'0';
					for(int i = 0; i<4; i++) {
						gamelog += getmove[i];
					}
//					if(chess.board[getXtogo][getYtogo] != null) gamelog += "00";
					gamelog += "00";
					Piece enp = chess.board[getXtogo][getYtogo];
					if(chess.board[getX][getY].type == 'P' && Math.abs(getYtogo - getY) == 1 && enp == null) {
						deadPiece[countdeadPiece] = chess.board[getXtogo-1][getYtogo];
						chess.board[getXtogo-1][getYtogo] = null;
						deadPiece[countdeadPiece].whendead = turn; 
						countdeadPiece ++;
					}
					if(enp != null) {
						deadPiece[countdeadPiece] = enp;
						deadPiece[countdeadPiece].whendead = turn;
						countdeadPiece ++;
					}
					chess.board[getX][getY].move(getXtogo, getYtogo, chess);
					
					if(chess.board[getXtogo][getYtogo].type == 'K' && Math.abs(getYtogo - getY) ==2 && chess.board[getXtogo][getYtogo].color == 'B') {
						if(getYtogo == 2) {
							chess.board[getXtogo][0].move(getXtogo, getYtogo+1, chess);
						}else
							chess.board[getXtogo][7].move(getXtogo, getYtogo-1, chess);
					}
					
					if(getmove[4] !=0) {
						promotionMoveTimes[promotionCount] = chess.board[getXtogo][getYtogo].movetimes;
						promotionCount++;
					switch(getmove[4]) {
					case 'Q' : chess.board[getXtogo][getYtogo] = new Queen(getXtogo, getYtogo, chess); gamelog += "Q";break;
					case 'R' : chess.board[getXtogo][getYtogo] = new Rook(getXtogo, getYtogo, chess); gamelog += "R";break;
					case 'B' : chess.board[getXtogo][getYtogo] = new Bishop(getXtogo, getYtogo, chess); gamelog += "B";break;
					case 'N' : chess.board[getXtogo][getYtogo] = new Knight(getXtogo, getYtogo, chess);gamelog += "N"; break;
					}
					chess.board[getXtogo][getYtogo].color = 'B';
					}else {
						gamelog += "0";
					}
					checked = Common.checked(chess, 'B');
					wchecked = Common.checked(chess, 'W');
					
					if(getmove[5] == 1) {
						System.out.println("체크메이트");
						if(wchecked)
							JOptionPane.showInternalMessageDialog(null, "Checkmate!!Winner is black");
						else if(checked)
							JOptionPane.showInternalMessageDialog(null, "Checkmate!!Winner is white");
						else 
							JOptionPane.showInternalMessageDialog(null, "Draw");
						checkmate = true;
					}
				}catch(ArrayIndexOutOfBoundsException ne) {}
				catch(NullPointerException ne) {}
				}
				else {
					if(gamelog.charAt(gamelog.length()-1) == '0') { 
						gamelog = Common.undo(gamelog, chess);
						countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
						turn--;
						} else {
							int undoX = gamelog.charAt(gamelog.length()-5)-'0';
							int undoY = gamelog.charAt(gamelog.length()-4)-'0';
							chess.board[undoX][undoY] = new Pawn(undoX, undoY, chess);
							chess.board[undoX][undoY].movetimes = promotionMoveTimes[promotionCount-1];
							promotionCount--;
							if(chess.board[undoX][undoY].color == 'W') chess.board[undoX][undoY].color = 'B';
							else if(chess.board[undoX][undoY].color == 'B') chess.board[undoX][undoY].color = 'W';
							gamelog = Common.undo(gamelog, chess);
							countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
							turn--;
						}
						if(gamelog.charAt(gamelog.length()-1) == '0') { 
							gamelog = Common.undo(gamelog, chess);
							countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
							turn--;
							
							} else {
								int undoX = gamelog.charAt(gamelog.length()-5)-'0';
								int undoY = gamelog.charAt(gamelog.length()-4)-'0';
								chess.board[undoX][undoY] = new Pawn(undoX, undoY, chess);
								chess.board[undoX][undoY].movetimes = promotionMoveTimes[promotionCount-1];
								promotionCount--;
								if(chess.board[undoX][undoY].color == 'W') chess.board[undoX][undoY].color = 'B';
								else if(chess.board[undoX][undoY].color == 'B') chess.board[undoX][undoY].color = 'W';
								gamelog = Common.undo(gamelog, chess);
								countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
								turn--;
					}
				}
			}
			} finally {
				closeAll(serverSocket, socket, br, pw);
			}
	}
	
	class MymouseListener implements MouseListener {
		int inputX = 0;
		int inputXtogo = 0;
		int inputY = 0;
		int inputYtogo = 0;
		int umj = 0; // 움직인 횟수
		
		@Override
		public void mouseClicked(MouseEvent e) {
			 if (e.getButton() == MouseEvent.BUTTON1) {

			if(checkmate) System.exit(0);
			if(turn %2 == 1) {
			umj = 0;
			checked = false;
			wchecked = false;
			if(clicktimes == 0) { // 처음 클릭했을때
			
			inputX = (e.getY()-20)/45; // 들어온 좌표를 Chess에 맞게 변환
			inputY = (e.getX()-20)/45;
			if(chess.board[inputX][inputY] == null) {
				JOptionPane.showInternalMessageDialog(null, "빈 칸입니다.");
				error = true;
			}else if(chess.board[inputX][inputY].color != 'W') {
				error = true;
				JOptionPane.showInternalMessageDialog(null, "백색의 턴입니다.");
			}else if(inputX>7 || inputY<0 || inputY >7 || inputX<0) {
				error = true;
				JOptionPane.showInternalMessageDialog(null, "잘못된 입력입니다.");
			}else if(chess.board[inputX][inputY] != null && !error) { // 찍은 것을 움직일 수 있고 빈 칸이 아닐 때
				cango = Common.CangoTest(chess, inputX, inputY, turn);
				try {
					Common.enPassant(inputX, inputY, getXtogo, getYtogo, chess, cango);
					} catch(NullPointerException ne) {} catch(ArrayIndexOutOfBoundsException ae) {} // 앙파상 
				if(Common.umj == 0) { //움직일수있는곳 개수가 0이면 못움직입니다.
					JOptionPane.showInternalMessageDialog(null, "그 말은 움직일 수 없습니다.");
					clicktimes =0;
				}else { // 움직일수 있는 곳의 개수가 0만 아니라면 다음으로 넘어갑니다.
					clicktimes ++;
				}
			}
			
			}else if(clicktimes == 1) {
				inputXtogo = (e.getY()-20)/45;
				inputYtogo = (e.getX()-20)/45; 
				if(cango[inputXtogo][inputYtogo]) {// 새로 입력받은 좌표가 아까 구해둔 움직일수 있는 데이터상으로 움직일 수 있을때
					Piece tmpP = chess.board[inputXtogo][inputYtogo];
					Piece tmpEnpP = null;
					if(inputXtogo+1<8) {
						tmpEnpP = chess.board[inputXtogo+1][inputYtogo];
					}
					Common.movePiece(inputX, inputY, inputXtogo, inputYtogo, chess);
					turn++;// 다음턴으로 넘어간다.
					sendtoserver[0] = (char)(7-inputX + '0');
					sendtoserver[1] = (char)(7-inputY + '0');
					sendtoserver[2] = (char)(7-inputXtogo + '0');
					sendtoserver[3] = (char)(7-inputYtogo + '0'); // server로 보낼 정보 조정
					for(int i = 0; i<4; i++) {
						gamelog += '7'-sendtoserver[i];
					}
					if(tmpP != null) {
						deadPiece[countdeadPiece] = tmpP;
						deadPiece[countdeadPiece].whendead = turn;
						countdeadPiece ++;
						gamelog +=countdeadPiece>9?countdeadPiece : "0"+countdeadPiece;
					}
					else if(tmpP == null && chess.board[inputXtogo][inputYtogo].type == 'P' && Math.abs(inputYtogo-inputY) ==1) {
						deadPiece[countdeadPiece] = tmpEnpP;
						deadPiece[countdeadPiece].whendead = turn;
						countdeadPiece ++;
						gamelog +=countdeadPiece>9?countdeadPiece : "0"+countdeadPiece;
					} else gamelog+= "00";
					error = false;
				}else { // 만약 움직일 수 없는 곳 좌표가 들어왔다면
					JOptionPane.showInternalMessageDialog(null, "그 곳으로는 이동할 수 없습니다.");
					error = true;
					clicktimes = 0;
				}// 못움직임
				
				
				for(int i = 0; i<8; i++) {
					for(int j = 0; j<8; j++) {
						cango[i][j] = false;
					}
				}// 더이상 움직일수있는자리에 별이 안떠도 좋다
				try {
				if(chess.board[inputXtogo][inputYtogo].type == 'P' ) {// 폰이
						if(inputXtogo == 0) {
							promotion = true;
							clicktimes = 2;
							promoX = inputYtogo;
							turn--;
						}
				}
				}catch(NullPointerException ne) {}
				if(promotion == false && !error) {
					clicktimes = 0;//프로모션이 안될경우 처음으로 돌아간다.
					gamelog += "0";
					System.out.println("askdfjposejifnnp");
				}
				
			}else if(clicktimes == 2) {//프로모션 한다
				int promoInputX = (e.getY()-20)/45;
				int promoInputY = (e.getX()-20)/45;
				promotionMoveTimes[promotionCount] = chess.board[inputXtogo][inputYtogo].movetimes;
				promotionCount++;
				if(promoInputY == inputYtogo) {
					switch(promoInputX) { // 뭘 클릭했느냐에 따라 프로모션을 한다.
					case 0 : chess.board[inputXtogo][inputYtogo] = new Queen(inputXtogo, inputYtogo, chess);
						sendtoserver[4] = 'Q';
						chess.board[inputXtogo][inputYtogo].color = 'W';
						gamelog += "Q";
						break;
					case 1 : chess.board[inputXtogo][inputYtogo] = new Rook(inputXtogo, inputYtogo, chess);
						sendtoserver[4] = 'R';
						chess.board[inputXtogo][inputYtogo].color = 'W';
						gamelog += "R";
						break;
					case 2 : chess.board[inputXtogo][inputYtogo] = new Bishop(inputXtogo, inputYtogo, chess);
						sendtoserver[4] = 'B';
						chess.board[inputXtogo][inputYtogo].color = 'W';
						gamelog += "B";
						break;
					case 3 :	chess.board[inputXtogo][inputYtogo] = new Knight(inputXtogo, inputYtogo, chess);
						sendtoserver[4] = 'N';
						chess.board[inputXtogo][inputYtogo].color = 'W';
						gamelog += "N";
						break;
					}clicktimes = 0;
					promotion = false; // 프로모션 끝
					turn++;
				
				}else {
					JOptionPane.showInternalMessageDialog(null, "잘못된 프로모션입니다.");
				}
				
				}
				
			}
			int checkumj = Common.checkmateTest(chess, 'B');
			
			checked = Common.checked(chess, 'B');
			wchecked = Common.checked(chess, 'W');
				if(checkumj == 0 && checked)  // 못움직이는데 흑이 체크당하면 백 승
				{System.out.println("체크메이트");
				sendtoserver[5] = 1;
				if(wchecked)
					JOptionPane.showInternalMessageDialog(null, "Checkmate!!Winner is black");
				else
					JOptionPane.showInternalMessageDialog(null, "Checkmate!!Winner is white");
				checkmate = true;
				}
				else if(checkumj == 0) { // 둘다아니면 무승부
					sendtoserver[5] = 1;
					JOptionPane.showInternalMessageDialog(null, "draw");
					checkmate = true;
				}
					
				if(clicktimes == 0 && !error) {
					pw.println(new String(sendtoserver));
					for(int i = 0; i<5; i++) {
						sendtoserver[i] = 0;
					}
				}
				 error = false;
			 }
			 if (e.getButton() == MouseEvent.BUTTON3) {
				if(gamelog.charAt(gamelog.length()-1) == '0') { 
				gamelog = Common.undo(gamelog, chess);
				countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
				turn--;
				} else {
					int undoX = gamelog.charAt(gamelog.length()-5)-'0';
					int undoY = gamelog.charAt(gamelog.length()-4)-'0';
					chess.board[undoX][undoY] = new Pawn(undoX, undoY, chess);
					chess.board[undoX][undoY].movetimes = promotionMoveTimes[promotionCount-1];
					promotionCount--;
					if(chess.board[undoX][undoY].color == 'W') chess.board[undoX][undoY].color = 'B';
					else if(chess.board[undoX][undoY].color == 'B') chess.board[undoX][undoY].color = 'W';
					gamelog = Common.undo(gamelog, chess);
					countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
					turn--;
				}
				if(gamelog.charAt(gamelog.length()-1) == '0') { 
					gamelog = Common.undo(gamelog, chess);
					countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
					turn--;
					
					} else {
						int undoX = gamelog.charAt(gamelog.length()-5)-'0';
						int undoY = gamelog.charAt(gamelog.length()-4)-'0';
						chess.board[undoX][undoY] = new Pawn(undoX, undoY, chess);
						chess.board[undoX][undoY].movetimes = promotionMoveTimes[promotionCount-1];
						promotionCount--;
						if(chess.board[undoX][undoY].color == 'W') chess.board[undoX][undoY].color = 'B';
						else if(chess.board[undoX][undoY].color == 'B') chess.board[undoX][undoY].color = 'W';
						gamelog = Common.undo(gamelog, chess);
						countdeadPiece = Common.undoAlive(deadPiece, chess, turn, countdeadPiece);
						turn--;
					}
				pw.println("U");
				
			 }
			
			 
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
		if(promotion){
			g.drawImage(Wqueen, promoX*45+30, 0*45 +55,  null);
			g.drawImage(Wrook, promoX*45+30,1*45 +55,  null);
			g.drawImage(Wbishop, promoX*45+30, 2*45 +55,  null);
			g.drawImage(Wknight, promoX*45+30, 3*45 +55,  null);
		}
		
		this.repaint();
	}
	public static String getIp(){
	    String result = null;
	    try {
	        result = InetAddress.getLocalHost().getHostAddress();
	    } catch (UnknownHostException e) {
	        result = "";
	    }
	   return result; 
	}
	public static void main(String[] args) {
		
		try {
			new ChessSwingServer(); //서버의 비즈니스 로직이 들어간 함수를 실행한다.
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
