package com.humblebundle;

import com.humblebundle.components.BoardSquare;
import com.humblebundle.components.ChessBoard;
import com.humblebundle.components.ChessPiece;
import com.humblebundle.enums.Color;
import com.humblebundle.enums.Type;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.StringTokenizer;

/**
 * Created by Summit on 9/10/15.
 */
public class Chess {

	private Deque<String> input;
	private ChessBoard gameBoard;
	private int legalMoves;
	private Color nextMove;
	private int uniquePieces;

	public Chess() {
		this.gameBoard = new ChessBoard();
		this.legalMoves = 0;
		this.uniquePieces = 0;
	}

	public void setInputs(String filename) {

		Deque<String> Deque = new ArrayDeque<String>();
		FileInputStream fis = null;
		BufferedReader br = null;

		try {
			fis = new FileInputStream(filename);

			br = new BufferedReader(new InputStreamReader(fis));

			String line = null;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if(line.startsWith("Next Move:")){
					String[] temp = line.split(":");
					nextMove = getColor(temp[1]);
				}else{
					Deque.push(line);
				}
			}
			System.out.println("\n\n");
		} catch (FileNotFoundException f) {
			f.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (FileNotFoundException f) {
				f.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.input = Deque;
	}

	public void setupBoard(){
		for (String s : input) {
			StringTokenizer st = new StringTokenizer(s, " :,");
			ChessPiece piece = new ChessPiece(getColor(st.nextToken()), getType(st.nextToken()));
			int xPos = Character.getNumericValue(st.nextToken().charAt(0)) - 10;
			int yPos = Integer.parseInt(st.nextToken());

			this.gameBoard.addPiece(piece, xPos, yPos);
		}
		this.gameBoard.printBoard();
	}

	public void getMoves(String filename) {

		setupBoard();

		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				if (!gameBoard.getSquareAtPos(x,y).isEmpty()) {
					ChessPiece piece = gameBoard.getSquareAtPos(x,y).occupiedPiece;
					if(piece.getPieceColor() == nextMove) {
						uniquePieces++;
						findValidMoves(piece, x, y);
					}
				}
			}
		}
		System.out.println(legalMoves + " legal moves ("+uniquePieces+" unique pieces) for "+nextMove+" player");
	}

	private void findValidMoves(ChessPiece piece, int x, int y) {

		switch (piece.getPieceType()) {
		case PAWN:
			getPawnMoves(piece.getPieceColor(), x, y);
			break;
		case QUEEN:
			getStraightMoves(piece, x, y);
			getDiagonalMoves(piece, x, y);
			break;
		case KING: 
			getKingMoves(piece.getPieceColor(), x, y);
			break;
		case ROOK:
			getStraightMoves(piece, x, y);
			break;
		case BISHOP: 
			getDiagonalMoves(piece, x, y);
			break;
		case KNIGHT: 
			getKnightMoves(piece.getPieceColor(), x, y);
			break;
		}

	}

	private void doMove(int checkX, int checkY, ArrayList<String> possibleMoves, Color color){
		BoardSquare moveSquare = gameBoard.getSquareAtPos(checkX, checkY);
		if (moveSquare.isEmpty()) {
			possibleMoves.add("<"+(char)(65+checkX) + ":" + checkY+">");
		}
		else {
			if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
				possibleMoves.add("<"+(char)(65+checkX) + ":" + checkY+">");
			}
		}
	}


	private void getPawnMoves(Color pawnColor, int x, int y) {

		ArrayList<String> possibleMoves = new ArrayList<>();
		int yOffset = pawnColor == Color.BLACK ? y-1 : y+1;
		Color oppositeColor = pawnColor == Color.BLACK ? Color.WHITE : Color.BLACK;

		if ((yOffset < 8) && (yOffset >= 0)){
			if (gameBoard.getSquareAtPos(x, yOffset).isEmpty()) {
				possibleMoves.add(pawnColor + " PAWN at <"+(char)(65+x)+":"+y+"> can move to <" + (char)(65+x) + ":" + yOffset+">");
			}

			if ((x+1) < 8) {
				BoardSquare square = gameBoard.getSquareAtPos((x+1), yOffset);
				if (!square.isEmpty() && square.getOccupiedPiece().getPieceColor() == oppositeColor) {
					possibleMoves.add(pawnColor + " PAWN at <"+(char)(65+x)+":"+y+"> can move to <"+(char)(65+x+1)+":"+yOffset+">");
				}
			}
			if ((x-1) > 0) {
				BoardSquare square = gameBoard.getSquareAtPos((x-1), yOffset);
				if (!square.isEmpty() && square.getOccupiedPiece().getPieceColor() == oppositeColor) {
					possibleMoves.add(pawnColor + " PAWN at <"+(char)(65+x)+":"+y+"> can move to <"+(char)(65+x-1)+":"+yOffset+">");
				}
			}

			// Pawn is at starting position and thus can move two places in front
			if (pawnColor.equals(Color.BLACK) && y == 6) {
				if(gameBoard.getSquareAtPos(x,y-1).isEmpty() && gameBoard.getSquareAtPos(x, y-2).isEmpty()) {
					possibleMoves.add(pawnColor + " PAWN at <"+(char)(65+x)+":"+y+"> can move to <" + (char)(65+x) + ":" + (y-2)+">");
				}
			} else if (pawnColor.equals(Color.WHITE) && y == 1) {
				if (gameBoard.getSquareAtPos(x, y + 1).isEmpty() && gameBoard.getSquareAtPos(x, y + 2).isEmpty()) {
					possibleMoves.add(pawnColor + " PAWN at <"+(char)(65+x)+":"+y+"> can move to <" + (char)(65+x) + ":" + (y + 2)+">");
				}
			}
		}

		if(possibleMoves.size() == 0) {
			System.out.println("No Moves for PAWN At <"+x+":"+y+">");
		} else {
			for (String str: possibleMoves){
				System.out.println(str);
			}
		}
	}

	private void getKingMoves(Color color, int x, int y){
		ArrayList<String> possibleMoves = new ArrayList<String>();
		// Getting NORTH moves
		int yNorth = y;
		yNorth++;
		if(yNorth < 8){
			doMove(x, yNorth, possibleMoves, color);
		}

		// Getting SOUTH moves
		int ySouth = y;
		ySouth--;
		if(ySouth >= 0){
			doMove(x, ySouth, possibleMoves, color);
		}

		// Getting EAST moves
		int xEast = x;
		xEast++;
		if(xEast < 8){
			doMove(xEast, y, possibleMoves, color);
		}

		// Getting WEST moves
		int xWest = x;
		xWest--;
		if(xWest >= 0){
			doMove(xWest, y, possibleMoves, color);
		}

		// Getting NORTHEAST moves
		yNorth = y;
		int xNorth = x;
		yNorth++;
		xNorth++;
		if(yNorth < 8 && xNorth < 8){
			doMove(xNorth, yNorth, possibleMoves, color);
		}

		// Getting SOUTHWEST moves
		ySouth = y;
		int xSouth = x;
		ySouth--;
		xSouth--;
		if(xSouth >= 0 && ySouth >= 0){
			doMove(xSouth, ySouth, possibleMoves, color);
		}

		// Getting SOUTHEAST moves
		xEast = x;
		int yEast = y;
		xEast++;
		yEast--;
		if(xEast < 8 && yEast >= 0){
			doMove(xEast, yEast, possibleMoves, color);
		}

		// Getting NORTHWEST moves
		xWest = x;
		int yWest = y;
		xWest--;
		yWest++;
		if(xWest >= 0 && yWest < 8){
			doMove(xWest, yWest, possibleMoves, color);
		}

		legalMoves += possibleMoves.size();

		if(possibleMoves.size() == 0) {
			System.out.println("No moves for KING at <"+x+":"+y+">");
		}

		for(String str:possibleMoves){
			System.out.println(color+" KING at <"+(char)(65+x)+":"+y+"> can move to "+str);
		}

	}

	private void getStraightMoves(ChessPiece piece, int x, int y) {

		ArrayList<String> possibleMoves = new ArrayList<String>();

		Color color = piece.getPieceColor();

		// Getting NORTH moves
		int yNorth = y;
		while (yNorth < 7) {
			yNorth++;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(x, yNorth);
			if (moveSquare.isEmpty()) {
				possibleMoves.add("<"+(char)(65+x) + ":" + yNorth+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
					possibleMoves.add("<"+(char)(65+x) + ":" + yNorth+">");
				}
				break;
			}
		}

		// Getting SOUTH moves
		int ySouth = y;
		while (ySouth > 0) {
			ySouth--;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(x, ySouth);
			if (moveSquare.isEmpty()) {
				possibleMoves.add("<"+(char)(65+x) + ":" + ySouth+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color){
					possibleMoves.add("<"+(char)(65+x) + ":" + ySouth+">");
				}
				break;
			}
		}

		// Getting EAST moves
		int xEast = x;
		while (xEast < 7) {
			xEast++;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(xEast, y);
			if (moveSquare.isEmpty()) {
				possibleMoves.add("<"+(char)(65+xEast) + ":" + y+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
					possibleMoves.add("<"+(char)(65+xEast) + ":" + y+">");
				}
				break;
			}
		}

		// Getting WEST moves
		int xWest = x;
		while (xWest > 0) {
			xWest--;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(xWest, y);
			if (moveSquare.isEmpty()){
				possibleMoves.add("<"+(char)(65+xWest) + ":" + y+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
					possibleMoves.add("<"+(char)(65+xWest) + ":" + y+">");
				}
				break;
			}
		}

		legalMoves += possibleMoves.size();

		if(possibleMoves.size() == 0) {
			System.out.println("No moves for "+piece.getPieceType()+" at <"+x+":"+y+">");
		}

		for(String str:possibleMoves){
			System.out.println(color+" "+piece.getPieceType()+" at <"+(char)(65+x)+":"+y+"> can move to "+str);
		}

	}

	private void getDiagonalMoves(ChessPiece piece, int x, int y) {

		ArrayList<String> possibleMoves = new ArrayList<String>();

		Color color = piece.getPieceColor();

		// Getting NORTHEAST moves
		int yNorth = y;
		int xNorth = x;
		while (yNorth < 7 && xNorth < 7) {
			yNorth++;
			xNorth++;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(xNorth, yNorth);
			if (moveSquare.isEmpty()) {
				possibleMoves.add("<"+(char)(65+xNorth) + ":" + yNorth+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
					possibleMoves.add("<"+(char)(65+xNorth) + ":" + yNorth+">");
				}
				break;
			}
		}

		// Getting SOUTHWEST moves
		int ySouth = y;
		int xSouth = x;
		while (ySouth > 0 && xSouth > 0) {
			ySouth--;
			xSouth--;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(xSouth, ySouth);
			if (moveSquare.isEmpty()) {
				possibleMoves.add("<"+(char)(65+xSouth) + ":" + ySouth+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color){
					possibleMoves.add("<"+(char)(65+xSouth) + ":" + ySouth+">");
				}
				break;
			}
		}

		// Getting SOUTHEAST moves
		int xEast = x;
		int yEast = y;
		while (xEast < 7 && yEast > 0) {
			xEast++;
			yEast--;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(xEast, yEast);
			if (moveSquare.isEmpty()) {
				possibleMoves.add("<"+(char)(65+xEast) + ":" + yEast+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
					possibleMoves.add("<"+(char)(65+xEast) + ":" + yEast+">");
				}
				break;
			}
		}

		// Getting NORTHWEST moves
		int xWest = x;
		int yWest = y;
		while (xWest > 0 && yWest < 7) {
			xWest--;
			yWest++;
			BoardSquare moveSquare = gameBoard.getSquareAtPos(xWest, yWest);
			if (moveSquare.isEmpty()){
				possibleMoves.add("<"+(char)(65+xWest) + ":" + yWest+">");
			}
			else {
				if(moveSquare.getOccupiedPiece().getPieceColor() != color) {
					possibleMoves.add("<"+(char)(65+xWest) + ":" + yWest+">");
				}
				break;
			}

		}

		legalMoves += possibleMoves.size();

		if(possibleMoves.size() == 0) {
			System.out.println("No moves for "+piece.getPieceType()+" at <"+x+":"+y+">");
		}

		for(String str:possibleMoves){
			System.out.println(color+" "+piece.getPieceType()+" at <"+(char)(65+x)+":"+y+"> can move to "+str);
		}
	}

	public void getKnightMoves(Color color, int x, int y) {

		ArrayList<String> possibleMoves = new ArrayList<String>();

		// Case 1
		int checkY = y + 2;
		int checkX = x + 1;
		if ( checkX < 8 && checkY < 8) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 2
		checkY = y + 2;
		checkX = x - 1;
		if (checkY < 8 && checkX >= 0) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 3
		checkY = y + 1;
		checkX = x + 2;
		if (checkX < 8 && checkY < 8) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 4
		checkY = y - 1;
		checkX = x + 2;
		if (checkY >=0 && checkX < 8) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 5
		checkX = x + 1;
		checkY = y - 2;
		if (checkX < 8 && checkY >= 0) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 6
		checkX = x - 1;
		checkY = y - 2;
		if (checkX >= 0 && checkY >= 0) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 7
		checkX = x - 2;
		checkY = y + 1;
		if (checkY < 8 && checkX >= 0) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		// Case 8
		checkY = y - 1;
		checkX = x - 2;
		if (checkX >= 0 && checkY >= 0) {
			doMove(checkX, checkY, possibleMoves, color);
		}

		legalMoves += possibleMoves.size();
		if(possibleMoves.size() == 0) {
			System.out.println("No moves for KNIGHT at <"+x+":"+y+">");
		}
		for(String str:possibleMoves){
			System.out.println(color+" KNIGHT at <"+(char)(65+x)+":"+y+"> can move to "+str);
		}
	}

	private Color getColor(String input) {
		switch (input) {
		case "White":
			return Color.WHITE;
		case "Black":
			return Color.BLACK;
		default:
			return null;
		}
	}

	private Type getType(String input) {
		switch (input) {
		case "Pawn":
			return Type.PAWN;
		case "King":
			return Type.KING;
		case "Queen":
			return Type.QUEEN;
		case "Rook":
			return Type.ROOK;
		case "Bishop":
			return Type.BISHOP;
		case "Knight":
			return Type.KNIGHT;
		default:
			return null;
		}
	}

}
