package chessGame;

public class Piece {
	char color;
	int x, swingx;// swing x = y
	int y, swingy; // swing y = x
	char type;
	int movetimes =0;
	boolean checked = false;
	int whendead = 0;
	Piece(int x, int y){
		if(x<=1) color = 'B'; else color = 'W';
		this.x = x;
		this.y = y;
		this.swingx = y * 45 + 30;
		this.swingy = x * 45 + 55;
	} // 생성자. 처음에 생성할때 사용하며, 위치에따라 다르게 생성됨
	void move(int x, int y, ChessBoard b) {
		b.board[x][y] = this;
		b.board[this.x][this.y]= null;
		int diffX = x-this.x;
		int diffY = y-this.y;
		int directionX =0, directionY = 0;
		if(diffX != 0) directionX = Math.abs(diffX)/diffX;
		if(diffY != 0) directionY = Math.abs(diffY)/diffY;
	
		
			
		
			this.x = x;
			this.y = y;
			this.swingx = y * 45 + 30;
			this.swingy = x * 45 + 55;
		
		movetimes ++;
	}  // 움직이는 함수. 이동하고 스윙 좌표들도 다 수정해주도록 함. chess의 xy랑 swing의 xy가 반대기때문에 반대로 설정
	boolean canmove(int x, int y, ChessBoard b) {
		
		return true;
	} // 오버라이드를 위한 것
	boolean checkedTest(ChessBoard b) {
		return false;
	} // 체크테스트 오버라이드용, 왕에게만 오버라이드시켜서 왕만 체크할수있도록 함
}
