package io.github.ollybishop.bishopsgambit.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.github.ollybishop.bishopsgambit.model.Square;

@TestInstance( Lifecycle.PER_CLASS )
class ApplicationFrameTest
{
    private static final int UI_DELAY_MILLIS = 20;

    private final ApplicationFrame frame = new ApplicationFrame();

    private final SquareComponent e2 = getSquareComponent( 'e', '2' );
    private final SquareComponent e4 = getSquareComponent( 'e', '4' );

    private boolean isFirstTest = true;

    private SquareComponent getSquareComponent( char file, char rank )
    {
        return frame.getSquareComponents().get( Square.getIndex( file, rank ) );
    }

    private void mouseEvent( SquareComponent squareComponent, int eventType, int modifiers )
    {
        try
        {
            SwingUtilities.invokeAndWait( () ->
            {
                MouseEvent event = new MouseEvent(
                    frame.getChessboardPane(),
                    eventType,
                    System.currentTimeMillis(),
                    modifiers,
                    squareComponent.getX(),
                    squareComponent.getY(),
                    1,
                    false,
                    MouseEvent.BUTTON1 );

                frame.getChessboardPane().dispatchEvent( event );
            } );

            Thread.sleep( UI_DELAY_MILLIS );
        }
        catch ( InvocationTargetException e )
        {
            throw new AssertionError( "Chess engine logic crashed during simulated move processing", e.getCause() );
        }
        catch ( InterruptedException e )
        {
            Thread.currentThread().interrupt();

            throw new RuntimeException( "Test execution thread was interrupted", e );
        }
    }

    private void click( SquareComponent squareComponent )
    {
        press( squareComponent );
        release( squareComponent );
    }

    private void press( SquareComponent squareComponent )
    {
        mouseEvent( squareComponent, MouseEvent.MOUSE_PRESSED, InputEvent.BUTTON1_DOWN_MASK );
    }

    private void release( SquareComponent squareComponent )
    {
        mouseEvent( squareComponent, MouseEvent.MOUSE_RELEASED, 0 );
    }

    @BeforeAll
    void beforeAll()
    {
        frame.setVisible( true );
    }

    @BeforeEach
    void createNewGame()
    {
        // Skip the first test because ApplicationFrame already creates a new Game on startup.
        // This relies on @TestInstance( Lifecycle.PER_CLASS ) preserving state between tests.
        if ( isFirstTest )
        {
            isFirstTest = false;
            return;
        }

        frame.newGame();
    }

    @AfterAll
    void afterAll()
    {
        JOptionPane.showMessageDialog(
            frame,
            "All automated tests complete. The application will now close.",
            ApplicationFrameTest.class.getSimpleName(),
            JOptionPane.INFORMATION_MESSAGE );
    }

    @Test
    void pressE2_releaseE4()
    {
        press( e2 );
        release( e4 );

        assertEquals( e2, frame.getFromSquare() );
        assertEquals( e4, frame.getToSquare() );
    }

    @Test
    void clickE2_clickE4()
    {
        click( e2 );
        click( e4 );

        assertEquals( e2, frame.getFromSquare() );
        assertEquals( e4, frame.getToSquare() );
    }
}
