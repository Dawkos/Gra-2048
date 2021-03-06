import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameScore {

    private static BestScore score;
    private final static String PATHXML = "src/game.xml";

    public static BestScore getScore() {
        return score;
    }

    public static void setScore(BestScore newScore) {
        score = newScore;
    }

    public static BestScore getScoreXML() throws ParserConfigurationException, SAXException, IOException {

        File fXmlFile = new File(PATHXML);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        NodeList nList = doc.getElementsByTagName("Score");

        BestScore tmp = new BestScore();
        tmp.setScore(Integer.parseInt(nList.item(0).getTextContent()));

        return tmp;

    }

    public static void setScoreXML(BestScore score) throws ParserConfigurationException, SAXException, IOException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(PATHXML);

        Node sc = doc.getElementsByTagName("Score").item(0);
        sc.setTextContent(String.valueOf(score.getScore()));

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(PATHXML));
        transformer.transform(source, result);

    }
}