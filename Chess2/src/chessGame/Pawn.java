package chessGame;

public class Pawn extends Piece{
	Pawn(int x, int y, ChessBoard b){
		super(x, y);
		this.type = 'P';
		b.board[x][y] = this;
	}

	@Override
	boolean canmove(int x, int y, ChessBoard b) {
		if(b.board[x][y] != null && b.board[x][y].color == this.color)
			return false;
		int absy = Math.abs(y-this.y);
		if(movetimes != 0) { // 처음 움직임이 아닐때 1칸만 움직여짐
			if(color == 'B') {
				if(((x == this.x+1 && y == this.y+1)||(x == this.x+1 && y == this.y-1)) && b.board[x][y] !=null) {
					return true; // 대각선으론 잡아먹음
				}
				if(x == this.x+1) {
					if(b.board[x][y] != null) return false;
					if(absy == 0) return true;
				}
				
				else return false;
			}else if(x == this.x-1 && x>=0){
				if(((y == this.y+1)||( y == this.y-1))&& b.board[x][y] !=null) {
					return true;
				}
				if(b.board[x][y] != null) return false;
				if(absy == 0) return true;
			}
			else return false;
		}
		else if(movetimes == 0){ // 처음엔 두칸까지 가능
			if(color == 'B') {
				if(x == this.x+1) {
					if((y == this.y+1||y == this.y-1) && b.board[x][y] !=null) { // 대각선 먹음
						return true;
					}
					if(b.board[x][y] != null) return false;
					if(absy == 0) return true;
				}else if(x == this.x + 2) {
					if(b.board[x][y] != null) return false;
					if(b.board[x-1][y] != null) return false;
					if(absy == 0) return true;
				}
				else return false;
			}else if(x == this.x-1) {
				if((y == this.y+1||y == this.y-1)&& b.board[x][y] !=null) {
					return true;
				}
				if(b.board[x][y] != null) return false;
				if(absy == 0) return true;
			}else if(x == this.x - 2) {
				if(b.board[x][y] != null) return false;
				if(b.board[x+1][y] != null) return false;
				if(absy == 0) return true;
			}
			else return false;
		}
		return false;
	}
}
