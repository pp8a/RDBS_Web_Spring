package com.epam.rd.autotasks.chesspuzzles;

import java.util.Collection;

public class ChessBoardImpl implements ChessBoard {
	private final ChessPiece[][] board = new ChessPiece[8][8];
	
	public ChessBoardImpl(Collection<ChessPiece> pieces) {
		init(pieces);
	}

	private void init(Collection<ChessPiece> pieces) {
		for (ChessPiece piece : pieces) {
			Cell cell = piece.getCell();
			if(cell != null) {
				board[8 - cell.number][cell.letter - 'A'] = piece;
			}
			
		}
		
	}

	@Override
	public String state() {
		StringBuilder state = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			ChessPiece[] chessPieces = board[i];
			for (int j = 0; j < chessPieces.length; j++) {
				ChessPiece chessPiece = chessPieces[j];
				char symbol = chessPiece == null ? '.' : chessPiece.toChar();
				state.append(symbol);
			}
			if (i < board.length - 1) {
	            // Добавляем новую строку только если это не последняя строка
	            state.append('\n');
	        }		
		}
		
		return state.toString();
	}

}
