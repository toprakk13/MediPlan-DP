import javax.xml.parsers.*;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 * DiagnosticCatalogue
 *
 * Responsible for:
 *   Step 1 -- loadFromXML()
 *             Parses diagnostic_catalogue.xml and builds the test map.
 *
 *   Step 2 -- computeDerivedCosts()
 *             Computes processing_cost for DERIVED tests.
 *             DERIVED tests depend exclusively on RAW tests.
 *             COMPOSITE tests always have no processing_cost.
 */
public class DiagnosticCatalogue {

    // =========================================================================
    // Inner class -- do NOT change field names or types
    // =========================================================================
    /**
     * Represents a single diagnostic test in the catalogue.
     *
     * RAW tests:
     *   - sampleType is one of: BLOOD, URINE, TISSUE, NONE
     *   - cost is the collection_cost read from XML
     *   - inputs is empty
     *
     * DERIVED tests:
     *   - sampleType is null (not applicable)
     *   - cost is computed by computeDerivedCosts() in Step 2
     *   - inputs contains only RAW test IDs
     *
     * COMPOSITE tests:
     *   - sampleType is null (not applicable)
     *   - initial cost is 0 (pure aggregation, no processing cost of its own)
     *   - inputs contains only DERIVED or COMPOSITE test IDs
     */
    public static class Test {

        public String       id;
        public String       name;
        public String       type;        // "RAW", "DERIVED", or "COMPOSITE"
        public String       sampleType;  // meaningful for RAW only
        public int          cost;        // collection_cost (RAW) or processing_cost (DERIVED) or 0 (COMPOSITE)
        public List<String> inputs;      // direct dependency IDs (empty for RAW)

        public Test() {
            inputs = new ArrayList<>();
        }

        /** Returns true if this test is of type RAW. */
        public boolean isRaw() {
            return "RAW".equalsIgnoreCase(type);
        }

        /** Returns true if this test is of type DERIVED. */
        public boolean isDerived() {
            return "DERIVED".equalsIgnoreCase(type);
        }

        /** Returns true if this test is of type COMPOSITE. */
        public boolean isComposite() {
            return "COMPOSITE".equalsIgnoreCase(type);
        }

        @Override
        public String toString() {
            return id + " [" + type + ", cost=" + cost + "]";
        }
    }

    // =========================================================================
    // Fields -- do NOT change names or types
    // =========================================================================
    /**
     * All tests in the catalogue, keyed by test ID.
     * Populated by loadFromXML(). LinkedHashMap preserves XML insertion order
     * which keeps output deterministic.
     */
    private Map<String, Test> tests = new LinkedHashMap<>();

    // =========================================================================
    // Step 1 -- TODO: implement this method
    // =========================================================================
    /**
     * Parses diagnostic_catalogue.xml and populates the tests map.
     * Uses standard Java javax.xml.parsers -- no external libraries.
     *
     * XML format:
     *   RAW:      <test id=".." name=".." type="RAW"
     *                   sample_type=".." collection_cost=".."/>
     *   DERIVED:  <test id=".." name=".." type="DERIVED">
     *               <input ref=".."/>  (only RAW refs)
     *             </test>
     *   COMPOSITE:<test id=".." name=".." type="COMPOSITE">
     *               <input ref=".."/>  (DERIVED or COMPOSITE refs)
     *             </test>
     *
     * @param filePath path to diagnostic_catalogue.xml
     */
    public void loadFromXML(String filePath) {
        // TODO: implement Step 1
    	try {
    		File xmlFile=new File(filePath);
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("test");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Test testObject=new Test();
                    
                    testObject.id = eElement.getAttribute("id");
                    testObject.name = eElement.getAttribute("name");
                    testObject.type = eElement.getAttribute("type");

                    if (testObject.type.equals("RAW")) {
                    	testObject.sampleType = eElement.getAttribute("sample_type");
                    	testObject.cost = Integer.parseInt(eElement.getAttribute("collection_cost"));
                        
                    } 

                    NodeList inputList = eElement.getElementsByTagName("input");
                    for (int i = 0; i < inputList.getLength(); i++) {
                        Element inputEl = (Element) inputList.item(i);
                        String refId = inputEl.getAttribute("ref");
                        testObject.inputs.add(refId);
                        
                    }
                    tests.put(testObject.id, testObject);
                }
            	}
    		
    	} 	
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }

    // =========================================================================
    // Step 2 -- TODO: implement this method
    // =========================================================================
    /**
     * Computes and assigns processing_cost for every DERIVED test.
     *
     * Cost rule:
     *   DERIVED tests depend only on RAW tests directly.
     *   Iterate over the test's direct inputs
     *   Count the number of distinct non-NONE sample types among them.
     *   That count is the processing_cost.
     *
     * COMPOSITE tests are left at cost = 0 and must be skipped here. 
     * They are pure aggregations and carry no processing cost of their own.
     *
     * Prints a summary line for each DERIVED test:
     *   <id>    processing_cost: <value>
     * surrounded by ##COST COMPUTATION## markers.
     */
    public void computeDerivedCosts() {
        System.out.println("##COST COMPUTATION##");

        // TODO: implement Step 2
        // Use System.out.printf("%-25s processing_cost: %d%n", id, cost);
        for(Test i: tests.values()) {
        	if(i.isDerived()) {
        		 Set<String> distSamp = new HashSet<>();// because hash no holds duplicate
        		for(String j:i.inputs) {
        			 Test inpTest=tests.get(j);
        			if(inpTest.sampleType!=null && !inpTest.sampleType.equals("NONE")) {
        				distSamp.add(inpTest.sampleType);
        			}
        		}
        		i.cost=distSamp.size();
        		System.out.printf(i.id+" processing_cost: "+ i.cost);
        	}	
        	}
        System.out.println("##COST COMPUTATION COMPLETED##");
        System.out.println();
    }

    // =========================================================================
    // Accessors -- do NOT change these
    // =========================================================================

    /** Returns the Test object for the given ID, or null if not found. */
    public Test getTest(String id) {
        return tests.get(id);
    }

    /** Returns an unmodifiable view of all tests in the catalogue. */
    public Map<String, Test> getAllTests() {
        return Collections.unmodifiableMap(tests);
    }
}
