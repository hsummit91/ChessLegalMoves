package com.humblebundle.components;

/**
 * Created by Summit on 9/10/15.
 */
public class BoardSquare {

    public ChessPiece occupiedPiece;
    public boolean isEmpty = true;
    
    public ChessPiece getOccupiedPiece() {
        return occupiedPiece;
    }

    public void setOccupiedPiece(ChessPiece occupiedPiece) {
        this.occupiedPiece = occupiedPiece;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
