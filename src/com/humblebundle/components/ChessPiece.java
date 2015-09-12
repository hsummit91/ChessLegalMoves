package com.humblebundle.components;
import com.humblebundle.enums.Color;
import com.humblebundle.enums.Type;
/**
 * Created by Summit on 9/10/15.
 */
public class ChessPiece {

	private Type pieceType;
	private Color pieceColor;

	public ChessPiece(Color color, Type type) {
		this.pieceColor = color;
		this.pieceType = type;
	}

	public Type getPieceType() {
		return pieceType;
	}

	public void setPieceType(Type pieceType) {
		this.pieceType = pieceType;
	}

	public Color getPieceColor() {
		return pieceColor;
	}

	public void setPieceColor(Color pieceColor) {
		this.pieceColor = pieceColor;
	}

}
