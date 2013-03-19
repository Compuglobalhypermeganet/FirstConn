package ffrmparser;
 
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class SAXXMLHandler extends DefaultHandler {
 
    private List<Equipo> equipos;
    private String tempVal;
    private Equipo tempEq;
 
    public SAXXMLHandler() {
        equipos = new ArrayList<Equipo>();
    }
 
    public List<Equipo> getEquipos() {
        return equipos;
    }
 
    // Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    		throws SAXException {
        // reset
        tempVal = "";
        if (qName.equalsIgnoreCase("equipo")) {
            // create a new instance of equipo
            tempEq = new Equipo();
        }
    }
 
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tempVal = new String(ch, start, length);
    }
 
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("equipo")) {
            // add it to the list
            equipos.add(tempEq);
        } else if (qName.equalsIgnoreCase("nombre")) {
            tempEq.setNombre(tempVal);
        } else if (qName.equalsIgnoreCase("tipo")) {
            tempEq.setTipo(tempVal);
        }
       
    }
}