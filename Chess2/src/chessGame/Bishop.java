package chessGame;

public class Bishop extends Piece{
	Bishop(int x, int y, ChessBoard b){
		super(x, y);
		this.type='B';
		b.board[x][y] = this;
	} // 생성자 오버라이드

	@Override
	boolean canmove(int x, int y, ChessBoard b) {
		int dx = x - this.x, dy = y - this.y;
		int absx = Math.abs(x-this.x);
		int absy = Math.abs(y-this.y);
		int dirx = 0;
		int diry = 0;
		if(dx != 0) {
			dirx = dx/Math.abs(dx);
		}
		if(dy != 0) {
			diry = dy/Math.abs(dy);
		}
		boolean cango=false;
		if(b.board[x][y] != null && b.board[x][y].color == this.color)
			return cango; // 가려는 곳에 이미 같은 색의 돌이 있다면 갈 수 없음
		if(absx == absy) { // 대각선으로 움직일 경우
			if(absx == 1) cango = true; //1칸갈경우, 위에서 return 안했다면 갈 수 있음
			for(int i = 1; i<absx; i ++) {
				if(b.board[this.x + dirx*i][this.y + diry*i] != null) {
					cango = false;
					break;
				} // x,y까지 가려하는데, 가는 길 중 하나라도 null이면 false
				else {
					cango = true;
				} // 전부 빈 칸이면 true
			}return cango;
		
		}else return false;
	}
}
