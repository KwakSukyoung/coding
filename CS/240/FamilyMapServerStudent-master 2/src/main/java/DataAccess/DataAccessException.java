package DataAccess;

/**
 *Deals with access exceptions
 */
public class DataAccessException extends Exception {

    /**
     * DataAccessException: Define here for exceptions while accessing to Data
     */
    DataAccessException(String message) {
        super(message);
    }
}