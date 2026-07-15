package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;

class IllegalMoveException extends IllegalArgumentException
{
    private IllegalMoveException( String message )
    {
        super( message );
    }

    static IllegalMoveException wrongPlayer( Piece piece, Player activePlayer )
    {
        String message = "%s cannot be moved because it is %s's turn.".formatted( piece, activePlayer );
        return new IllegalMoveException( message );
    }

    static IllegalMoveException illegalPieceMove( Square from, Square to )
    {
        String message = "%s cannot legally move from %s to %s.".formatted( from.getPiece(), from, to );
        return new IllegalMoveException( message );
    }
}
