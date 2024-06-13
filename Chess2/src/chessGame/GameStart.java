package chessGame;

import java.util.Scanner;

public class GameStart {
	public static void main(String[] args) {
		int turn = 1;
		Scanner sc = new Scanner(System.in);
		ChessBoard chess = new ChessBoard();
		chess.printBoard();
		boolean playing = true;
		while(playing) {
			System.out.print("어디의 말을 움직일까요 : ");
			String input1 = sc.next();
			int before = Integer.parseInt(input1);
			int beforex = before/10;
			int beforey = before%10;
			if(chess.board[beforex][beforey] == null) {
				System.out.println("빈 칸입니다.");
				continue;
			}
			if(turn%2 == 1 && chess.board[beforex][beforey].color != 'W') {
				System.out.println("백색의 턴입니다!");
				continue;
			}
			
			if(turn%2 == 0 &&chess.board[beforex][beforey].color != 'B') {
				System.out.println("흑색의 턴입니다!");
				continue;
			}
			
			
			if(before >=80 || before<0 || beforey >7) {
				System.out.println("잘못된 입력입니다");
				continue;
			}
			
			chess.printWhereCanGo(beforex, beforey);
			if(!ChessBoard.canmove) {
				System.out.println("그 말은 움직일 수 없습니다.");
				continue;
			}
			System.out.print("어디로 움직일까요 : ");
			
			String input2 = sc.next();
			int after = Integer.parseInt(input2);
			int afterx = after/10;
			int aftery = after%10;
			if(before >=80 || before<0 || beforey >7) {
				System.out.println("잘못된 입력입니다");
				continue;
			}
		
			if(chess.board[beforex][beforey].canmove(afterx, aftery, chess))
				chess.board[beforex][beforey].move(afterx, aftery, chess);
			else {
				System.out.println("이동할 수 없습니다.");
				continue;
			}
			chess.printBoard();
			
			for(int i = 0; i<8; i++) {
				for(int j = 0; j<8; j++) {
					try {
						if(chess.board[i][j].checkedTest(chess))
							System.out.println("체크 상태입니다.");
					}catch(NullPointerException e) {continue;}
				}
			}
			
			turn ++;
		}
		
		
		sc.close();
	}
}
