package chessGame;

public class ChessBoard {
	Piece[][] board = new Piece[8][8]; // 체스 판을 피스 객체의 2차원 array로 만듦
	static boolean canmove = false; // 기본적으로 false로 canmove를 만듦. canmove함수로 이게 트루가 되면 움직일 수 있는 것. ChessStart에서 사용
	// 이게 false면 그 말은 움직일 수 없는 것.
	ChessBoard(){ // 체스보드 생성자, 객체 생성 시 체스 기본 규칙에 맞게 말들을 생성함
		Pawn[] BP = new Pawn[8];
		Pawn[] WP = new Pawn[8];
		for(int i = 0; i<8; i++) {
			BP[i] = new Pawn(1, i, this);
			WP[i] = new Pawn(6, i, this);
		}
		Rook BR1 = new Rook(0, 0, this);
		Rook BR2 = new Rook(0, 7, this);
		Rook WR1 = new Rook(7, 0, this);
		Rook WR2 = new Rook(7, 7, this);
		Knight BN1 = new Knight(0,1,this);
		Knight BN2 = new Knight(0,6,this);
		Knight WN1 = new Knight(7,1,this);
		Knight WN2 = new Knight(7,6,this);
		Bishop BB1 = new Bishop(0,2,this);
		Bishop BB2 = new Bishop(0,5,this);
		Bishop WB1 = new Bishop(7,2,this);
		Bishop WB2 = new Bishop(7,5,this);
		Queen WQ = new Queen(0,3,this);
		Queen BQ = new Queen(7,3,this);
		King BK = new King(0,4,this);
		King WK = new King(7,4,this);
	}

	void printBoard() { // java에서 보드를 프린트하는 함수.
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board[i].length; j++) {
				if(board[i][j] != null)
					System.out.print(""+board[i][j].color + board[i][j].type + "\t");
				else	System.out.print("-\t");
			}
			System.out.println();
			
		}
	}
	void printWhereCanGo(int x, int y) { // x,y가 어디로 움직일 수 있는지 출력하는 함수. ㅇ
		canmove = false;
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				if(board[x][y].canmove(i, j,this)) {
					System.out.print("*");
					canmove = true;
				}// 움직일 수 있는 칸에 * 출력
				if(board[i][j] != null)
					System.out.print(""+board[i][j].color + board[i][j].type + "\t");
				else if(!board[x][y].canmove(i, j,this)) System.out.print("-\t");
				else System.out.print("\t"); 
				// 갈수있으면 *, 빈칸이면 -, 뭐 있는 칸이면 그 말의 특징 프린트
			}
			System.out.println();
		}
	}
	
	
	
}
