package domainmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This is an extension on DefaultHandler. It provides character data handling,
 * and parsing functionality.
 * <p>
 * Developers should extend this class and implement the appropriate handler
 * methods. As mentioned above, character data handling is built in so instead
 * of overriding the characters() method see the resetCharacters() and
 * getCharacters() methods.
 * <p>
 * There are several parse methods available. The String version should be used
 * when parsing a string that already exists in memory. If the XML document is
 * in a file or if the document is streamed from a process the File or stream
 * parse methods should be used. If the document is available as a file or
 * stream the string version of the parse() method should not be used as it is
 * not as memory efficient.
 * <p>
 * Whatever the result of the parse process is it should be accessed after the
 * parsing is complete via a getter method. Not all handlers will produce a
 * returnable result. For example a parser may perform CRUD operations on a
 * database as it parses the document and write a parsing report to a file on
 * disk.
 * 
 * @author <a href="mailto:justin.r.knowles@lowes.com">Justin R. Knowles </a>
 */
public abstract class DefaultHandler extends org.xml.sax.helpers.DefaultHandler {

    private StringBuffer characters;

    /**
     * Parses the passed XML String.
     * <p>
     * This is for parsing XML that is contained in a String.
     * 
     * @param string
     *            The XML to parse.
     * @throws Exception
     *             When the parsing fails.
     */
    public void parse(String string) throws Exception {
        SAXParserFactory.newInstance().newSAXParser().parse(
                new InputSource(new StringReader(string.trim())), this);
    }

    /**
     * Parses a document that is streamed in over the supplied input stream.
     * <p>
     * This is for parsing XML that is streamed into the system.
     * 
     * @param stream
     *            The stream of XML to parse.
     * @throws Exception
     *             When the parsing fails.
     */
    public void parse(InputStream stream) throws Exception {
        SAXParserFactory.newInstance().newSAXParser().parse(
                new InputSource(stream), this);
        stream.close();
    }

    /**
     * Parses a file based XML document.
     * <p>
     * This is for parsing XML files.
     * 
     * @param file
     *            The xml file to parse.
     * @throws Exception
     *             When the parsing fails.
     */
    public void parse(File file) throws Exception {
        this.parse(new FileInputStream(file));
    }

    /**
     * startElement
     * 
     * @param namespaceURI
     * @param sName
     * @param qName
     * @param attrs
     * @throws SAXException
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) throws SAXException {

        resetCharacters();
    }

    /**
     * endElement
     * 
     * @param namespaceURI
     * @param sName
     * @param qName
     * @throws SAXException
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String namespaceURI, String sName, String qName)
            throws SAXException {

        getCharacters(true);
    }

    /**
     * characters
     * 
     * @param ch
     * @param start
     * @param length
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length) {
        characters.append(ch, start, length);
    }

    /**
     * Resets the character variable.
     * <p>
     * The character variable represents the character data between the current
     * set of tags. For each new set of tags the character variable needs to be
     * reset or it will contain the character from previous tags. This method is
     * typically called first thing in the startElement handler method.
     */
    protected void resetCharacters() {
        characters = new StringBuffer();
    }

    /**
     * Returns the character data that is between the current set of tags.
     * <p>
     * The character variable represents the character data between the current
     * set of tags. Optionally the value can be squeezed and trimed (all extra
     * whitespace characters are removed an no leading or trailing white space).
     * 
     * @param squeeze
     *            true if the character data should be squeezed and trimed.
     * @return the character data that is between the current set of tags.
     */
    protected String getCharacters(boolean squeeze) {
        return (squeeze) ? squeeze(characters.toString()).trim() : characters
                .toString();
    }

    /**
     * squeeze Removes all extra whitespace characters from the passed String.
     * All occurrences of consecutive white space characters as defined by
     * Character#isWhitespace(char) are reduced down to one whitespace character
     * (a space ' '). A new modified String is returned. The string is not
     * trimed.
     * 
     * @param s
     *            the String to modify
     * @return a new String without repeated whitespace characters.
     */
    private static String squeeze(String s) {

        char[] oldString = s.toCharArray();
        int oldLength = oldString.length;
        char[] newString = new char[oldLength];
        int newLength = 0;
        char currentChar;
        boolean consecutive = false;

        for (int i = 0; i < oldLength; ++i) {
            currentChar = oldString[i];
            if (Character.isWhitespace(currentChar)) {
                if (!consecutive) {
                    consecutive = true;
                    newString[newLength++] = ' ';
                }
            }
            else {
                consecutive = false;
                newString[newLength++] = currentChar;
            }
        }

        return new String(newString, 0, newLength);
    }
}