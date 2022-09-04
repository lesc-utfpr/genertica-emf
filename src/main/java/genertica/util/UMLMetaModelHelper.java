package genertica.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This was originally a helper class from DERCS.
 */
public class UMLMetaModelHelper {
    public static List<String> getElementOfExpression(java.lang.String value, boolean returnDelims) {
        //TODO this is very wrong and should be replaced with a proper expression-parser in the future
        StringTokenizer parser = new StringTokenizer(value, " ()|&<>=+-*/", returnDelims);
        ArrayList<java.lang.String> tokens = new ArrayList<String>();

        while(parser.hasMoreTokens()){
            java.lang.String token = parser.nextToken();

            // when encountering a " or ', merge tokens until finding the same symbol again to close the string
            // this is only correct in some cases, but it is the original behavior,
            // so it stays until proper expression parsing is added
            if (token.contains("'")) {
                java.lang.String next = parser.nextToken();
                while (!next.contains("'")) {
                    token += " " + next;
                    next = parser.nextToken();
                }
            }

            if (token.contains("\"")) {
                java.lang.String next = parser.nextToken();
                while (!next.contains("\"")) {
                    token += " " + next;
                    next = parser.nextToken();
                }
            }

            tokens.add(token);
        }

        return tokens;
    }
}
