package gui;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import pieces.Piece;
import pieces.Piece.Typ;
import players.Player.Colour;

public class Graphics {

	private Image whitePawn;
	private Image whiteKnight;
	private Image whiteBishop;
	private Image whiteRook;
	private Image whiteQueen;
	private Image whiteKing;

	private Image blackPawn;
	private Image blackKnight;
	private Image blackBishop;
	private Image blackRook;
	private Image blackQueen;
	private Image blackKing;

	public Graphics() {
		this.whitePawn = importImage(Colour.WHITE, Typ.PAWN);
		this.whiteKnight = importImage(Colour.WHITE, Typ.KNIGHT);
		this.whiteBishop = importImage(Colour.WHITE, Typ.BISHOP);
		this.whiteRook = importImage(Colour.WHITE, Typ.ROOK);
		this.whiteQueen = importImage(Colour.WHITE, Typ.QUEEN);
		this.whiteKing = importImage(Colour.WHITE, Typ.KING);

		this.blackPawn = importImage(Colour.BLACK, Typ.PAWN);
		this.blackKnight = importImage(Colour.BLACK, Typ.KNIGHT);
		this.blackBishop = importImage(Colour.BLACK, Typ.BISHOP);
		this.blackRook = importImage(Colour.BLACK, Typ.ROOK);
		this.blackQueen = importImage(Colour.BLACK, Typ.QUEEN);
		this.blackKing = importImage(Colour.BLACK, Typ.KING);
	}

	private Image importImage(Colour colour, Typ type) {
		Image image = null;

		try {
			String imageURL = String.format("/img/%s_%s.png", colour, type);
			image = ImageIO.read(getClass().getResource(imageURL));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	public Image getImage(Piece piece) {
		return getImage(piece.getColour(), piece.getType());
	}

	public Image getImage(Colour colour, Typ type) {
		Image image = null;

		switch (colour) {
		case WHITE:
			switch (type) {
			case PAWN:
				image = whitePawn;
				break;
			case KNIGHT:
				image = whiteKnight;
				break;
			case BISHOP:
				image = whiteBishop;
				break;
			case ROOK:
				image = whiteRook;
				break;
			case QUEEN:
				image = whiteQueen;
				break;
			case KING:
				image = whiteKing;
				break;
			}
			break;
		case BLACK:
			switch (type) {
			case PAWN:
				image = blackPawn;
				break;
			case KNIGHT:
				image = blackKnight;
				break;
			case BISHOP:
				image = blackBishop;
				break;
			case ROOK:
				image = blackRook;
				break;
			case QUEEN:
				image = blackQueen;
				break;
			case KING:
				image = blackKing;
				break;
			}
			break;
		}

		return image;
	}

}
