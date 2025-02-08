package main.java.gui;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JPanel;

import main.java.Orderable;

public class OrderedJPanel extends JPanel
{
    @Override
    protected final void addImpl( Component comp, Object constraints, int index )
    {
        if ( !(comp instanceof Orderable) )
            throw new IllegalArgumentException( "The component being added must be an instance of Orderable." );

        List<Orderable> components = Arrays.stream( getComponents() )
                                           .map( c -> (Orderable) c )
                                           .toList();

        // Get the index of the component before which the new component should be inserted
        index = IntStream.range( 0, components.size() )
                         .filter( i -> components.get( i ).compareTo( (Orderable) comp ) >= 0 )
                         .findFirst()
                         .orElse( -1 );

        super.addImpl( comp, constraints, index );
    }
}
