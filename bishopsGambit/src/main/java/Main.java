package main.java;

import java.awt.EventQueue;

import main.java.game.Game;
import main.java.gui.GUI;

public class Main
{
    /**
     * Launch the application.
     */
    public static void main( String[] args )
    {
        Game game = new Game();

        EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    GUI frame = new GUI( game );
                    frame.setVisible( true );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );
    }
}
