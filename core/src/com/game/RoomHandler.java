package com.game;

import com.entity.enemies.*;
import com.entity.items.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.battlechess.BattleChess;
import com.entity.Player;
import com.framework.GameState;

import java.sql.Time;

public class RoomHandler implements Runnable{
	private static int ranStartGate = 0;
	private static int roomCount = 0;
	public RoomHandler() {
		Space[][] boardMaker = new Space[17][17];
		if(Board.isFirstRoom) {
			
			for(int r = 0;r<boardMaker.length;r++)
				for(int c = 0;c<boardMaker[r].length;c++) {
					if(r<2 || r > 13 || c < 2)
						boardMaker[r][c] = new Space(null,Space.State.CLEAR);
					else if(r == 2 || r == 13 || c == 2)
						boardMaker[r][c] = new Space(null,Space.State.WALL);
					else if(r >= 2 && r <= 13 && c == 16) {
						boardMaker[r][c] = new Space(null,Space.State.WALL);
						if(r == 12 && c == 16) {
							boardMaker[r][c].setExit(true);
							Rook rook = new Rook(15,r);
							boardMaker[r][c].setEntity(rook);
						}
					}
					else {
						if(r == 3 && c == 3) {
							Player.getPlayer().setPos(r,c);
							boardMaker[r][c] = new Space(Player.getPlayer(),Space.State.FLOOR);
						}else
							boardMaker[r][c] = new Space(null,Space.State.FLOOR);
					}
				}
			Board.getBoard().newBoard(boardMaker);
			Board.getBoard().getSpaces()[3][3].setEntity(Player.getPlayer());
		}else
			Board.getBoard().newBoard(genBoard(boardMaker));
			
	}
	
	private Space[][] genBoard(Space[][] board) {
		roomCount++;
		int twistCount = 0;
		int fillCount = 0;
		int rewardCount = 0;
		int coverCount = 0;
		int ranStartMinR = 0;
		int ranStartMaxR = 0;
		while((ranStartMaxR - ranStartMinR) % 3 != 1) {
		 ranStartMinR = (int)(Math.random() * 4) * 3;
		 ranStartMaxR = ranStartMinR + 5 + (int)(Math.random() * (12 - ranStartMinR));
		
		if(ranStartMaxR >= 17)
			ranStartMaxR = 16;
		}
		  ranStartGate = ranStartMinR + (int)(Math.random() * (ranStartMaxR - ranStartMinR));

		if(ranStartGate >= ranStartMaxR)
			ranStartGate = ranStartMaxR - 1;
		if(ranStartGate <= ranStartMinR)
			ranStartGate = ranStartMinR + 1;
		GameState.getInstance().getScreen().setFading(1);
		long startTime = TimeUtils.millis();
		while(TimeUtils.timeSinceMillis(startTime) < 500) {}
		Player.getPlayer().setPos(1,ranStartGate);

		for(int r = 0;r<board.length;r++) {
			if(r >= ranStartMinR && r <= ranStartMaxR) {
				board[r][0] = new Space(null,Space.State.WALL);
				if(r == ranStartGate)
					board[r][0].setEntrance(true);
				if((r == ranStartMinR && r == 0) || (r == ranStartMaxR && r == 16))
					board[r][1] = new Space(null,Space.State.WALL);
			}
			else
				board[r][0] = new Space(null,Space.State.CLEAR);
			
		}
		for(int r = 0;r<board.length;r++)
			for(int c = 1;c<board[r].length;c++) 
				if(r < ranStartMaxR && r > ranStartMinR && c > 0 && c < 4)
					board[r][c] = new Space(null,Space.State.FLOOR);
				else if(r == ranStartMaxR && r == ranStartMinR && c > 0 && c < 4)
					board[r][c] = new Space(null,Space.State.FLOOR);
				else if(board[r][c] ==  null)
					board[r][c] = new Space(null,Space.State.CLEAR);
		
		
		
		
		return board;
	}
	
	@Override
	public void run() {
		while(!Board.exit) {
			//System.out.println("Checking for new Board!");
		}
		Board.exit = false;
		(new Thread(new RoomHandler())).start();
	}
	
	

}
