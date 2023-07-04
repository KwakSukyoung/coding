package Result;

/**
 * class for FillResult
 */
public class FillResult {
    /**
     * message: message that you get
     */
    private String message;

    /**
     * success: notify if the request went through
     */
    private boolean success;

    /**
     * Constructor
     */
    public FillResult(String message, boolean success) {this.message = message; this.success = success;}
}
