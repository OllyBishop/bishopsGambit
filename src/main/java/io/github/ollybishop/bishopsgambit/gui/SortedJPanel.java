package io.github.ollybishop.bishopsgambit.gui;

import java.awt.Component;
import java.util.stream.IntStream;

import javax.swing.JPanel;

import io.github.ollybishop.bishopsgambit.util.Sortable;

/**
 * Custom version of {@link JPanel} which only allows {@link Sortable} components to be added.
 */
class SortedJPanel extends JPanel
{
    @Override
    protected final void addImpl( Component comp, Object constraints, int index )
    {
        if ( !( comp instanceof Sortable ) )
            throw new IllegalArgumentException( comp.getClass().getSimpleName() +
                                                " must implement Sortable to be added to a SortedJPanel." );

        if ( comp.getParent() == this )
            return;

        Component[] components = getComponents();

        // Find the index at which to insert the new component to maintain sort order
        index = IntStream.range( 0, components.length )
                         .filter( i -> ( (Sortable) components[ i ] ).compareTo( (Sortable) comp ) > 0 )
                         .findFirst()
                         .orElse( -1 );

        super.addImpl( comp, constraints, index );
    }
}
