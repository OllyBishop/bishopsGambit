package main.java.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

import main.java.Orderable;

public class OrderedJPanel extends JPanel
{
    private final List<Orderable> orderedList = new ArrayList<>();

    @Override
    protected void addImpl( Component comp, Object constraints, int index )
    {
        addImpl( comp, constraints );
    }

    private void addImpl( Component comp, Object constraints )
    {
        if ( !(comp instanceof Orderable) )
            throw new IllegalArgumentException( "The component being added must be an instance of Orderable." );

        // Find the component next to which the new component should be inserted
        Optional<Orderable> optional = orderedList.stream()
                                                  .filter( o -> o.compareTo( comp ) >= 0 )
                                                  .findFirst();

        if ( optional.isPresent() )
        {
            int index = orderedList.indexOf( optional.get() );

            super.addImpl( comp, constraints, index );
            orderedList.add( index, (Orderable) comp );
        }
        else
        {
            super.addImpl( comp, constraints, -1 );
            orderedList.add( (Orderable) comp );
        }
    }

    @Override
    public void remove( int index )
    {
        super.remove( index );
        orderedList.remove( index );
    }
}
