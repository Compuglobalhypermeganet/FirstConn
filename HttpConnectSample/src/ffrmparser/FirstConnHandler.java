package ffrmparser;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class FirstConnHandler extends DefaultHandler{
	
	private String urlRead, urlBase, urlFinal;
	private String a;
	
	public FirstConnHandler() {
		urlBase = new String();
		urlFinal = new String();
		urlRead = new String();
		a = new String();
		a = "a";
	}
	
	public String getUrlBase(){
		return urlBase;
	}

	public String getUrlFinal(){
		return urlFinal;
	}
	
	public String getA(){
		return a;
	}
	
	// Event Handlers
	public void startElement(String uri, String localName, String qName)
            throws SAXException {
		urlRead = "";
        if (qName.equalsIgnoreCase("urb")) {
        	urlBase = "";
            a = a + "d";
        } else if (qName.equalsIgnoreCase("urf")) {
        	urlFinal = "";
            a = a + "e";
        }
		
	}
	
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        urlRead = new String(ch, start, length);
        a = a + "f";
    }
 
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("urb")) {
            urlBase = urlRead;
            a = a + "b";
        } else if (qName.equalsIgnoreCase("urf")) {
            urlFinal = urlRead;
            a = a + "c";
        }
    }


}
