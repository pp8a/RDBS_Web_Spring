package com.epam.rd.autotasks.chesspuzzles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.rd.autotasks.chesspuzzles.Bishop;
import com.epam.rd.autotasks.chesspuzzles.Cell;
import com.epam.rd.autotasks.chesspuzzles.King;
import com.epam.rd.autotasks.chesspuzzles.Knight;
import com.epam.rd.autotasks.chesspuzzles.Pawn;
import com.epam.rd.autotasks.chesspuzzles.Queen;
import com.epam.rd.autotasks.chesspuzzles.Rook;

@Configuration
public class Default {	
	@Bean
	public King blackKing() {
		return new King(Cell.cell('E', 8), 'K');
	}
	@Bean
	public King whiteKing() {
		return new King(Cell.cell('E', 1), 'k');
	}
	@Bean
	public Queen blackQueen() {
		return new Queen(Cell.cell('D', 8), 'Q');
	}
	@Bean
	public Queen whiteQueen() {
		return new Queen(Cell.cell('D', 1), 'q');
	}
	@Bean
	public Rook blackRook1() {
		return new Rook(Cell.cell('A', 8), 'R');
	}
	@Bean
	public Rook blackRook2() {
		return new Rook(Cell.cell('H', 8), 'R');
	}
	@Bean
	public Rook whiteRook1() {
		return new Rook(Cell.cell('A', 1), 'r');
	}
	@Bean
	public Rook whiteRook2() {
		return new Rook(Cell.cell('H', 1), 'r');
	}
	@Bean
	public Bishop blackBishop1() {
		return new Bishop(Cell.cell('C', 8), 'B');
	}
	@Bean
	public Bishop blackBishop2() {
		return new Bishop(Cell.cell('F', 8), 'B');
	}
	@Bean
	public Bishop whiteBishop1() {
		return new Bishop(Cell.cell('C', 1), 'b');
	}
	@Bean
	public Bishop whiteBishop2() {
		return new Bishop(Cell.cell('F', 1), 'b');
	}
	@Bean
	public Knight blackKnight1() {
		return new Knight(Cell.cell('B', 8), 'N');
	}
	@Bean
	public Knight blackKnight2() {
		return new Knight(Cell.cell('G', 8), 'N');
	}
	@Bean
	public Knight whiteKnight1() {
		return new Knight(Cell.cell('B', 1), 'n');
	}
	@Bean
	public Knight whiteKnight2() {
		return new Knight(Cell.cell('G', 1), 'n');
	}
	@Bean
	public Pawn blackPawn1() {
		return new Pawn(Cell.cell('A', 7), 'P');
	}
	@Bean
	public Pawn blackPawn2() {
		return new Pawn(Cell.cell('B', 7), 'P');
	}
	@Bean
	public Pawn blackPawn3() {
		return new Pawn(Cell.cell('C', 7), 'P');
	}
	@Bean
	public Pawn blackPawn4() {
		return new Pawn(Cell.cell('D', 7), 'P');
	}
	@Bean
	public Pawn blackPawn5() {
		return new Pawn(Cell.cell('E', 7), 'P');
	}
	@Bean
	public Pawn blackPawn6() {
		return new Pawn(Cell.cell('F', 7), 'P');
	}
	@Bean
	public Pawn blackPawn7() {
		return new Pawn(Cell.cell('G', 7), 'P');
	}
	@Bean
	public Pawn blackPawn8() {
		return new Pawn(Cell.cell('H', 7), 'P');
	}
	@Bean
	public Pawn whitePawn1() {
		return new Pawn(Cell.cell('A', 2), 'p');
	}
	@Bean
	public Pawn whitePawn2() {
		return new Pawn(Cell.cell('B', 2), 'p');
	}
	@Bean
	public Pawn whitePawn3() {
		return new Pawn(Cell.cell('C', 2), 'p');
	}
	@Bean
	public Pawn whitePawn4() {
		return new Pawn(Cell.cell('D', 2), 'p');
	}
	@Bean
	public Pawn whitePawn5() {
		return new Pawn(Cell.cell('E', 2), 'p');
	}
	@Bean
	public Pawn whitePawn6() {
		return new Pawn(Cell.cell('F', 2), 'p');
	}
	@Bean
	public Pawn whitePawn7() {
		return new Pawn(Cell.cell('G', 2), 'p');
	}
	@Bean
	public Pawn whitePawn8() {
		return new Pawn(Cell.cell('H', 2), 'p');
	}
}
