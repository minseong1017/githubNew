package chessGame;

public class King extends Piece{
	
	@Override
	boolean checkedTest(ChessBoard b) {
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				try {
					if(b.board[i][j].canmove(this.x, this.y, b)) {
					if(b.board[i][j].color != this.color) return true;
				}
			}catch(NullPointerException e) {continue;}
			}
		}
		
		return false;
	} // 체크당했는지 테스트. 피스에서 오버라이드했으며, 말이 왕일 경우에만 위의 테스트를 진행함. 모든 칸의 말을 테스트해서
	// 색이 다른 말이 이 위치에 올 수 있다면 체크 상태를 리턴함.

	King(int x, int y, ChessBoard b){
		super(x, y);
		this.type = 'K';
		b.board[x][y] = this;
	}

	@Override
	boolean canmove(int x, int y, ChessBoard b) { 
		try {
		if(this.movetimes == 0 && b.board[this.x][this.y+1] == null && b.board[this.x][this.y+2] == null && b.board[this.x][this.y+3].movetimes ==0)
			if(x == this.x &&y == this.y+2)return true;
		if(this.movetimes == 0 && b.board[this.x][this.y-1] == null && b.board[this.x][this.y-2] == null && b.board[this.x][this.y-3] == null && b.board[this.x][this.y-4].movetimes ==0)
			if(x == this.x &&y == this.y-2)return true;
		} catch(NullPointerException e) {} // 캐슬링 가능한지 테스트. 
		if(b.board[x][y] != null && b.board[x][y].color == this.color)
			return false;
		if(Math.abs(x-this.x) <=1 && Math.abs(y-this.y)<= 1) return true;
		else return false; // 왕은 그냥 한칸차이고 같은색말없으면 움직일 수 있음
	
	}
}
