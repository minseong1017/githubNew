package chessGame;

public class Knight extends Piece{
	Knight(int x, int y, ChessBoard b){
		super(x, y);
		this.type = 'N';
		b.board[x][y] = this;
	}

	@Override
	boolean canmove(int x, int y, ChessBoard b) {
		if(b.board[x][y] != null && b.board[x][y].color == this.color)
			return false;
		int absx = Math.abs(x-this.x);
		int absy = Math.abs(y-this.y);
		if(absx>0 && absy >0 && absx + absy == 3) return true;
		else return false;
	} // 어느방향이든 0보다 큰 이동을 하고, 이동한 좌표의 절대값의 합이 3이면 이동가능. 얘는 사이에 말 있어도 움직일 수 있기 때문에 예외 처리 안함.
	
}
