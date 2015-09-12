package com.humblebundle.components;

/**
 * Created by Summit on 9/10/15.
 */
public class ChessBoard {

	private BoardSquare[][] gameBoard = new BoardSquare[8][8];

	public ChessBoard() {
		initBoard();
	}

	private void initBoard() {
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				gameBoard[x][y] = new BoardSquare();
			}
		}
	}

	public void printBoard() {
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				String s = gameBoard[x][y].isEmpty ? "X" : gameBoard[x][y].getOccupiedPiece().getPieceType().toString();
				System.out.print(s);
			}
			System.out.println();
		}
		
		System.out.println("\n\n");
	}

	public BoardSquare getSquareAtPos(int x, int y) {

		return gameBoard[x][y];
	}

	public void addPiece(ChessPiece piece, int x, int y) {
		BoardSquare sq = getSquareAtPos(x, y);
		if (sq.isEmpty) {
			sq.setOccupiedPiece(piece);
			sq.isEmpty = false;
			gameBoard[x][y] = sq;
		} else System.err.println("Adding invalid pos "+x+" "+y);
	}


	public class InvalidPositionException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidPositionException(String message) {
			super(message);
		}
	}
}
