package io.github.ollybishop.bishopsgambit.game;

import io.github.ollybishop.bishopsgambit.model.Square;

class NoPieceOnSquareException extends IllegalStateException
{
    NoPieceOnSquareException( Square from )
    {
        super( "Cannot move a piece from empty square %s.".formatted( from ) );
    }
}
