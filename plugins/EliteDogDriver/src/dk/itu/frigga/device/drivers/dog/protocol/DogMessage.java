/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.dog.protocol;

import dk.itu.frigga.utility.XmlHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public abstract class DogMessage {

  protected Document doc;
  protected String id;
  protected int priority;
  private static DocumentBuilder builder = null;
  private static final String BASICXML =
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
          + "<dogmessage xmlns=\"http://elite.polito.it/domotics/dog2/DogMessage\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://elite.polito.it/domotics/dog2/DogMessage DogMessage.xsd\" >"
          + "</dogmessage>";

  //Initialize
  static {
    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException ex) {
    }
  }

  public DogMessage()
  {
    priority = 0;
  }

  protected void initialize() throws SAXException, IOException
  {
    doc = newBasicTemplate();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  private static Document newBasicTemplate() throws SAXException, IOException {
    return builder.parse(new ByteArrayInputStream(BASICXML.getBytes("UTF-8")));
  }

  protected static void setPriority(Document doc, int priority) {
    doc.getDocumentElement().setAttribute("priority", "" + priority);
  }

  protected static void setMessageId(Document doc, String id) {
    doc.getDocumentElement().setAttribute("id", id);
  }

  protected static void setSessionId(Document doc, String session) {
    Element root = XmlHelper.getFirstChildElement(doc.getDocumentElement());
    NodeList list = root.getElementsByTagName("session");
    Element current_session = null;
    if (list.getLength() > 0) {
      current_session = XmlHelper.getFirstElement(list);
    }
    if (current_session == null) {
      current_session = doc.createElement("session");
      doc.getDocumentElement().appendChild(current_session);
    }

    current_session.setTextContent(session);
  }

  public String generateXmlString(String session) {
    if (!(id == null) && !id.isEmpty()) {
      setMessageId(doc, id);
    }
    if (priority != 0) {
      setPriority(doc, priority);
    }

    setSessionId(doc, session);

    return xmlToString(doc);
  }

  /*
   * Helper class from:
   * http://www.coderanch.com/how-to/java/DocumentToString
   */
  private static String xmlToString(Node node) {
    try {
      Source source = new DOMSource(node);
      StringWriter stringWriter = new StringWriter();
      Result result = new StreamResult(stringWriter);
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.transform(source, result);
      return stringWriter.getBuffer().toString();
    } catch (TransformerException e) {
      //should never happend, its generateded throug dom Document builder
    }
    return null;
  }
}
