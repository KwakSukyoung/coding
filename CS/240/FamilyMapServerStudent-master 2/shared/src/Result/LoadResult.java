package Result;

/**
 * class for LoadResult
 */

public class LoadResult {
    /**
     * message: message that you get
     */
    private String message;

    /**
     * success: notify if it went through
     */
    private boolean success;

    /**
     * constructor
     */
    public LoadResult(String message, boolean success) {this.message = message; this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}

