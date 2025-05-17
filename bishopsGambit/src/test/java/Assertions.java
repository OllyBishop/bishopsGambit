package test.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.function.Executable;

public class Assertions
{
    /**
     * Asserts that execution of the given <b>executable</b> throws an exception of the
     * <b>expectedType</b>, and the message of the exception thrown is equal to the
     * <b>expectedMessage</b>.
     * 
     * @param expectedType    the type (class) of the exception we expect to be thrown
     * @param executable      the executable that causes the exception to be thrown
     * @param expectedMessage the message we expect the exception to have
     * @return the exception that was thrown
     */
    public static Exception assertThrowsWithMessage( Class<? extends Exception> expectedType,
                                                     Executable executable,
                                                     String expectedMessage )
    {
        Exception actual = assertThrows( expectedType, executable );
        assertEquals( expectedMessage, actual.getMessage() );
        return actual;
    }

    public static void assertSameNotNull( Object expected, Object actual )
    {
        assertNotNull( expected );
        assertNotNull( actual );
        assertSame( expected, actual );
    }
}
