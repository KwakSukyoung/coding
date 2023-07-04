package Help;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * writing json file
 */
public class writeString {
    /**
     * constructor
     */
    public writeString() {
    }
    /**
    The writeString method shows how to write a String to an OutputStream.
    */
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
