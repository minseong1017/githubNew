package chessGame;

public class Queen extends Piece {
	Queen(int x, int y, ChessBoard b){
		super(x, y);
		this.type = 'Q';
		b.board[x][y] = this;
	}
	// Canmove x-this.x == 0이고, y-this.y != 0이거나 반대 혹은 	
	//move(x,y,b) 들어왔을때 Math.abs(x- this.x) == Math.abs(y-this.y)

	@Override
	boolean canmove(int x, int y, ChessBoard b) { // 룩 + 비숍
		if(b.board[x][y] != null && b.board[x][y].color == this.color)
			return false;
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
		if((absx == 0 && absy !=0) ){
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
		
		if(absx == absy) {
			if(absx == 1) cango = true;
			for(int i = 1; i<absx; i ++) {
				if(b.board[this.x + dirx*i][this.y + diry*i] != null) {
					cango = false;
					break;
				}
				else {
					cango = true;
				}
			}return cango;
		
		}
		return cango;
	}

	
}
