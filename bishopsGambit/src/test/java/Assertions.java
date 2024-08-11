package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.function.ThrowingRunnable;

public class Assertions
{
    public static void assertThrowsWithMessage( Class<? extends Throwable> expectedThrowable,
                                                ThrowingRunnable runnable,
                                                String expectedMessage )
    {
        Throwable e = assertThrows( expectedThrowable, runnable );
        assertEquals( expectedMessage, e.getMessage() );
    }

    public static void assertSameNotNull( Object expected, Object actual )
    {
        assertNotNull( expected );
        assertNotNull( actual );
        assertSame( expected, actual );
    }
}
