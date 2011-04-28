package dk.itu.frigga.protocol;

/**
 * Created by IntelliJ IDEA.
 * User: Tommy
 * Date: 10-03-11
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public class UnexpectedElementException extends ProtocolSyntaxException {
    private final String expectedElement;
    private final String actualElement;

    public UnexpectedElementException(final String expectedElement, final String actualElement)
    {
        this.expectedElement = expectedElement;
        this.actualElement = actualElement;
    }

    @Override
    public String toString() {
        return "UnexpectedElementException, " +
               "expected: '" + expectedElement + "', " +
               "got: '" + actualElement + "'";
    }
}
