package chessGame;

public class Rook extends Piece{
	Rook(int x, int y, ChessBoard b){
		super(x, y);
		this.type = 'R';
		b.board[x][y] = this;
	}

	@Override
	boolean canmove(int x, int y, ChessBoard b) {
		int dx = x - this.x, dy = y - this.y;
		int absx = Math.abs(x-this.x);
		int absy = Math.abs(y-this.y);
		int dirx = 0;
		int diry = 0;
		if(dx != 0) {
			dirx = dx/absx;
		}
		if(dy != 0) {
			diry = dy/absy;
		}
		boolean cango = false;
		if(b.board[x][y] != null && b.board[x][y].color == this.color)
			return false;
		if((absx == 0 && absy !=0) ){ // 둘중 하나만 0이고 나머지는 0이 아닐경우 움직일 수 있음.
			if(absy == 1) return true;
			for(int i = 1; i<absy; i++) {
				if(b.board[this.x][this.y+diry*i] !=null) {
					cango = false;
					break;
				}else cango = true;
			}return cango;
		}else if (absx !=0 && absy ==0) {
			if(absx == 1) return true;
			for(int i = 1; i<absx; i++) {
				if(b.board[this.x+dirx*i][this.y] !=null) {
					cango = false;
					break;
				}else cango = true;
			}return cango;
		}
		return cango;
		
		
	}
	
}
