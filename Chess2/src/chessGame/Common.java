package chessGame;

public class Common {
	static int umj = 0;
	static int checkumj = 0;
	public static boolean[][] CangoTest(ChessBoard chess, int inputX, int inputY, int turn) { // 갈수있는지 테스트
		umj = 0;
		boolean cango[][] = new boolean[8][8]; 
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				try{
				cango[i][j] = false;
				if(chess.board[inputX][inputY].canmove(i, j,chess)) { // 움직일 수 있을 경우
					cango[i][j] = true;	// 움직일 수 있다 선언
					umj++; // 움직일수있는곳 개수 +1
					if(cango[i][j]) {// 근데
						Piece p = chess.board[i][j]; // 임의로 가려는 곳에 있는 자리에 피스가 있으면 여기 저장해두고
						chess.board[inputX][inputY].move(i, j, chess); // 거기 직접 가봤더니
						if(turn %2 == 1){ // 내가 체크 당하고 있으면
							boolean wchecked = checked(chess, 'W'); 
							if(wchecked) {cango[i][j] = false; umj --;} // 거긴 못움직여
						}else if(turn%2 == 0){
							boolean checked = checked(chess, 'B');
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
		return cango;
	}
	public static void enPassant(int inputX, int inputY, int getXtogo, int getYtogo, ChessBoard chess, boolean[][] cango) {
		if(Math.abs(inputY-getYtogo)==1 &&chess.board[inputX][inputY].type == 'P' &&chess.board[getXtogo][getYtogo].type == 'P' &&chess.board[getXtogo][getYtogo].movetimes == 1&& getXtogo ==3
				&& inputX ==getXtogo)
			cango[inputX-1][getYtogo] = true; 
	}
	
	public static void movePiece(int inputX, int inputY, int inputXtogo, int inputYtogo, ChessBoard chess) {
		Piece enp = chess.board[inputXtogo][inputYtogo];
		chess.board[inputX][inputY].move(inputXtogo, inputYtogo, chess); // 움직인다.
		if(chess.board[inputXtogo][inputYtogo].type == 'K' && Math.abs(inputYtogo-inputY) ==2) {
			try {// 만약 왕이 두칸을 움직였다면(캐슬링)
				if(chess.board[inputXtogo][inputYtogo].color == 'B') {
				if(inputYtogo == 1) {
					chess.board[inputXtogo][0].move(inputXtogo, inputYtogo+1, chess);
				}else {
					chess.board[inputXtogo][7].move(inputXtogo, inputYtogo-1, chess);// 룩이 같이 움직인다.
				}
				}else {
					if(inputYtogo == 2) {
						chess.board[inputXtogo][0].move(inputXtogo, inputYtogo+1, chess);
					}else {
						chess.board[inputXtogo][7].move(inputXtogo, inputYtogo-1, chess);// 룩이 같이 움직인다.
					}
				}
				
			}catch (NullPointerException ne) {}
				
		}
		try {// 옆으로 움직였는데 원래 있던 자리가 비어있으면 잡아먹은 취급
			if(chess.board[inputXtogo][inputYtogo].type == 'P' && Math.abs(inputYtogo-inputY) == 1 && enp == null) {

					chess.board[inputXtogo+1][inputYtogo] = null;
			}
		}catch (NullPointerException ne) {}
	}
	public static void moveUndo(int inputX, int inputY, int inputXtogo, int inputYtogo, ChessBoard chess) {
		Piece enp = chess.board[inputXtogo][inputYtogo];
		chess.board[inputX][inputY].move(inputXtogo, inputYtogo, chess); // 움직인다.
		if(chess.board[inputXtogo][inputYtogo].type == 'K' && Math.abs(inputYtogo-inputY) ==2) {
			try {// 만약 왕이 두칸을 움직였다면(캐슬링)
				if(inputYtogo == 1) {
					chess.board[inputXtogo][0].move(inputXtogo, inputYtogo+1, chess);
				}else
					chess.board[inputXtogo][7].move(inputXtogo, inputYtogo-1, chess);// 룩이 같이 움직인다.
			
			}catch (NullPointerException ne) {}
				
		}
	}
	
	public static int checkmateTest(ChessBoard chess, char c) { //현재 움직일 수 있는 말이 있는지
		checkumj = 0;
		outer : 
		for(int k = 0; k<8; k++) {
			for(int l = 0; l<8; l++) {
				for(int i = 0; i<8; i++) {
					for(int j = 0; j<8; j++) {
						try{
							if(chess.board[k][l].color == c && chess.board[k][l].canmove(i, j,chess)) { // 입력받은 x y 좌표에서 이동할 수 있는지
								checkumj++; // 모든 좌표를 확인해서
								if(chess.board[k][l].canmove(i, j,chess)) {
									Piece p = chess.board[i][j];
									chess.board[k][l].move(i, j, chess);
									boolean checked = checked(chess, c);
									if(checked) {checkumj --;} 
									chess.board[i][j].move(k, l, chess);
									chess.board[i][j] = p;
									chess.board[k][l].movetimes -= 2;
									if(checkumj !=0) break outer;
								}
							}
					}catch(NullPointerException ne) {continue;}
					}
			
				}
			}
		}
		return checkumj;
	}
	public static boolean checked(ChessBoard chess, char c) {
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				try {
					if(chess.board[i][j].checkedTest(chess)) {
						if(chess.board[i][j].color == c)	 // 모든 좌표에서 현재 좌표까지 체크테스트를 진행한다. 색은 char로
							return true;
					}
				}catch(NullPointerException ne) {continue;}
			}
		}
		return false;
	}
	public static String undo(String gamelog, ChessBoard chess) {
		 try {
			 int undoX = gamelog.charAt(gamelog.length()-5)-'0';
			 int undoY =gamelog.charAt(gamelog.length()-4)-'0';
			 int undoXtogo = gamelog.charAt(gamelog.length()-7)-'0';
			 int undoYtogo = gamelog.charAt(gamelog.length()-6)-'0';
			 Common.moveUndo(undoX, undoY, undoXtogo, undoYtogo, chess);
			 chess.board[undoXtogo][undoYtogo].movetimes-=2;
			 gamelog = gamelog.substring(0, gamelog.length()-7);
			 return gamelog;
			 }catch(NullPointerException ne) {System.out.println("널에러");} catch(StringIndexOutOfBoundsException se) {System.out.println("스트링에러");}
			 catch(ArrayIndexOutOfBoundsException ae) {System.out.println("어레이에러");}
		 return gamelog;
	}
	public static int undoAlive(Piece[] deadPiece, ChessBoard chess, int turn, int count) {
		try {	
			if(count - 1 >= 0) {
			if(deadPiece[count-1].whendead == turn) {
				chess.board[deadPiece[count-1].x][deadPiece[count-1].y] = deadPiece[count-1];
				deadPiece[count-1] = null;
				count --;
				return count;
			}
			}
		}catch (NullPointerException ne) {}
		return count;
	}
}
