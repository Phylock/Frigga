/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.frigga.device.drivers.eye;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author phylock
 */
public class LookLoader {

  public static void load(File file, ViewPanel view) throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document dom = db.parse(file);

    Element docEle = dom.getDocumentElement();

    //get a nodelist of elements
    NodeList nl = docEle.getElementsByTagName("image");
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {

        //get the employee element
        Element el = (Element) nl.item(i);
        String url = file.getParent() + File.separator + el.getAttribute("url");
        
        view.setImage(ImageIO.read(new File(url)));
        setImage(dom, el, view);
        view.repaint();
      }
    }
  }

  private static void setImage(Document doc, Element element, ViewPanel view) {
    view.getPoints().clear();
    //get a nodelist of elements
    NodeList nl = element.getElementsByTagName("point");
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {

        //get the employee element
        Element el = (Element) nl.item(i);
        int x = Integer.parseInt(el.getAttribute("x"));
        int y = Integer.parseInt(el.getAttribute("y"));
        String lookat = el.getAttribute("lookat");

        view.getPoints().add(new Point(x, y, lookat));

      }
    }

  }
}
