import javax.xml.parsers.*;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
/**
 * DiagnosticRequest
 *
 * Parses diagnostic_requests.xml and exposes:
 *   - single_target : the test ID for top-down patient burden analysis (Step 3)
 *   - all_targets   : list of test IDs for bottom-up hospital cost analysis (Step 4)
 *
 * XML structure:
 *   <requests>
 *     <single_target ref="overall_health"/>
 *     <all_targets>
 *       <target ref="overall_health"/>
 *       <target ref="cardiovascular_risk"/>
 *     </all_targets>
 *   </requests>
 *
 */
public class DiagnosticRequest {

    private String       singleTarget;
    private List<String> allTargets;

    private DiagnosticRequest() {}

    /**
     * Parses the given XML file and returns a populated DiagnosticRequest.
     *
     * @param filePath path to diagnostic_requests.xml
     * @return populated DiagnosticRequest
     */
    public static DiagnosticRequest loadFromXML(String filePath) {
        DiagnosticRequest req = new DiagnosticRequest();
        req.allTargets = new ArrayList<>();

        /** TODO: Parse the given XML file
        *  return a populated DiagnosticRequest
        *  set singleTarget and allTargets
        */
        try {
        	
        	File xmlFile=new File(filePath);
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList singleNodeL = doc.getElementsByTagName("single_target");
            if (singleNodeL.getLength() > 0) {
                  Element singleElement = (Element) singleNodeL.item(0);
               req.singleTarget = singleElement.getAttribute("ref");
         }
            NodeList targetNodeL = doc.getElementsByTagName("target");
            for (int i = 0; i < targetNodeL.getLength(); i++) {
                 Node nNode = targetNodeL.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element targetElement = (Element) nNode;
                    String ref = targetElement.getAttribute("ref");
                    if (!ref.isEmpty()) {
                    	req.allTargets.add(ref);
                    	}
                	}
            	}
        	}
        
        catch(Exception e) {
        	
        				}

        return req;
    	}

    /** Returns the single target test ID for top-down analysis. */
    public String getSingleTarget() {
        return singleTarget;
    }

    /** Returns the list of target test IDs for bottom-up analysis. */
    public List<String> getAllTargets() {
        return Collections.unmodifiableList(allTargets);
    }
}
