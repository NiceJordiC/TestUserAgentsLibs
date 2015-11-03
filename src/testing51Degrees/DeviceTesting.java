package testing51Degrees;

//jCalduch

import fiftyone.mobile.detection.Match;
import fiftyone.mobile.detection.Provider;
import fiftyone.mobile.detection.factories.MemoryFactory;
import fiftyone.mobile.detection.factories.StreamFactory;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DeviceTesting {	
	// FiftyOne Provider object - used to access data and perform match.
	
	public static final boolean GET_USER_AGENTS_FROM_FILE = false;
	
	private static Provider provider;
	
	// Path to FiftyOne device data file.
	private static final String PATH_TO_DAT_FILE = "/home/jordicalduchnpaw/Documents/Libraries/51Degrees-EnterpriseV3_1.dat";
	
	// User agent string of the device in question.
	private static final String USER_AGENT = "Mozilla/5.0 (BlackBerry; U; "
			+ "BlackBerry 9900; en) AppleWebKit/534.11+ (KHTML, like Gecko) "
			+ "Version/7.1.0.346 Mobile Safari/534.11+";
	
	private static final String USER_AGENT_WAKI = "Mozilla/5.0 (SmartHub; SMART-TV; U; Linux/SmartTV+2013; Maple2012) AppleWebKit/535.20+ (KHTML, like Gecko) SmartTV Safari/535.20+";

	public static LinkedHashMap<String, String> valuesMap51Degrees 	= new LinkedHashMap<>();
	public static LinkedHashMap<String, String> valuesMapMaxMind	= new LinkedHashMap<>();
	public static Map<String, String> comparisonMap			= new HashMap<>();
		
	private static final String CSV_FILE_PATH_WAKI = "/home/jordicalduchnpaw/Documents/user_agents_data_wuakitv.csv";
	private static final String CSV_FILE_PATH_GAIAM = "/home/jordicalduchnpaw/Documents/user_agents_data_gaiam.csv";
	private static final String CSV_FILE_RESULT_51DEGREE = "/home/jordicalduchnpaw/Documents/Testing51_result.csv";
	private static final String CSV_FILE_RESULT_UALIB = "/home/jordicalduchnpaw/Documents/TestingUALib_result.csv";
	private static final String CSV_FILE_RESULT_PARAMS = "/home/jordicalduchnpaw/Documents/Params51Deg.csv";
	private static final String CSV_FILE_RESULT_PARAMS_UA = "/home/jordicalduchnpaw/Documents/ParamsUALib.csv";
	
	
	public static void compareLibraries(Map map51Degrees, Map mapMaxMind){
//		for (Set<> key : map51Degrees.keySet())
//		{
//			
//		}
	}
	
	public static void checkMatchValues(Match match){
		// Fetch specific property
		try {			
			//Let's make a map, it's more useful
			valuesMap51Degrees.put("deviceType", match.getValues("DeviceType").toString());
			valuesMap51Degrees.put("browserName", match.getValues("BrowserName").toString());
			valuesMap51Degrees.put("vendor", match.getValues("DeviceType").toString());
			valuesMap51Degrees.put("browserVendor", match.getValues("BrowserVendor").toString());
			valuesMap51Degrees.put("hwVendor", match.getValues("HardwareVendor").toString());
			valuesMap51Degrees.put("browserVersion", match.getValues("BrowserVersion").toString());
			valuesMap51Degrees.put("platformVersion", match.getValues("PlatformVersion").toString());
			valuesMap51Degrees.put("hwName", match.getValues("HardwareName").toString());
			valuesMap51Degrees.put("hwFamily", match.getValues("HardwareFamily").toString());

			//Now the same with MaxMind Library
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkMatchValuesUAlib(ReadableUserAgent agent){
		// Fetch specific property
		// Let's make a map, it's more useful
		valuesMapMaxMind.put("DeviceName", agent.getName().toString());
		valuesMapMaxMind.put("Producer", agent.getProducer().toString());
		valuesMapMaxMind.put("TypeName", agent.getTypeName().toString());
		valuesMapMaxMind.put("DeviceCategory", agent.getDeviceCategory().toString());
		valuesMapMaxMind.put("Family", agent.getFamily().toString());
		valuesMapMaxMind.put("Os", agent.getOperatingSystem().toString());
		valuesMapMaxMind.put("Type", agent.getType().toString());
		valuesMapMaxMind.put("VersionName", agent.getVersionNumber().toString());
		valuesMapMaxMind.put("Url", agent.getUrl().toString());
	}
	
	private static void writeMatchValuesForThisAgent(FileWriter writer) {
		try {		
			for (Map.Entry<String,String> entry :valuesMap51Degrees.entrySet()){
				writer.append( entry.getKey() + ":			" + entry.getValue());
				writer.append('\n');
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private static void writeCSVMatchValuesForThisAgent(CSVWriter csvWriter, String userAgent) {
		String[] splittedEntries;
		String entries = "";
		for (Map.Entry<String, String> entry : valuesMap51Degrees.entrySet()) {
			//entries = entries + entry.getKey() + "#" + entry.getValue() + "#";
			entries = entries + entry.getValue() + "#";
		}
		splittedEntries = (userAgent + "#" + entries).split("#");
		csvWriter.writeNext(splittedEntries);
	}
	
	private static void writeCSVMatchValuesForThisAgentUALib(CSVWriter csvWriter, String userAgent) {
		String[] splittedEntries;
		String entries = "";
		for (Map.Entry<String, String> entry : valuesMapMaxMind.entrySet()) {
			//entries = entries + entry.getKey() + "#" + entry.getValue() + "#";
			entries = entries + entry.getValue() + "#";
		}
		splittedEntries = (userAgent + "#" + entries).split("#");
		csvWriter.writeNext(splittedEntries);
	}
		
	public static void main(String[] args) throws IOException {
		// Initialize provider with the data file to use.
		// MemoryFactory is faster but uses more resources
		
		//++++++++++++++ UAdetector++++++++++++++++
		UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
		ReadableUserAgent agent;
		
		//++++++++++++++ 51Degrees+++++++++++++++++
		//provider = new Provider(StreamFactory.create(PATH_TO_DAT_FILE));
		provider = new Provider(MemoryFactory.create(PATH_TO_DAT_FILE));
		
		FileWriter writer;
		writer = new FileWriter(CSV_FILE_RESULT_51DEGREE);
		CSVWriter csvWriter = new CSVWriter(new FileWriter(CSV_FILE_RESULT_51DEGREE), '\t');
		CSVWriter csvWriterUALib = new CSVWriter(new FileWriter(CSV_FILE_RESULT_UALIB), '\t');
			
		if (GET_USER_AGENTS_FROM_FILE){	
			BufferedReader br;
			String userAgentLine;
			br = new BufferedReader(new FileReader(CSV_FILE_PATH_WAKI));
			CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH_WAKI), '\t');
			
			String [] nextLine;
			reader.readNext(); //Jump the first line because it only has titles
			
			boolean enteredOnce = false;
			
		     while ((nextLine = reader.readNext()) != null) {
		    	 
		    	//51Deg -  Match object contains results of detection.
				Match match = provider.match(nextLine[0]);
		
				//UAlib - Agent object has the results of detection
				agent = parser.parse(nextLine[0]);
				
				checkMatchValues(match);
				checkMatchValuesUAlib(agent);
				
				if (!enteredOnce){
					String entries = "";
					String userAgent = "UserAgent#";
					for (Map.Entry<String, String> entry : valuesMap51Degrees.entrySet()) {
						entries = entries + entry.getKey() + "#";
					}
					csvWriter.writeNext((userAgent+entries).split("#"));
					
					entries = "";
					for (Map.Entry<String, String> entry : valuesMapMaxMind.entrySet()) {
						entries = entries + entry.getKey() + "#";
					}
					csvWriterUALib.writeNext((userAgent+entries).split("#"));
					
					enteredOnce=true;
				}

				writeCSVMatchValuesForThisAgent(csvWriter, nextLine[0]);
				writeCSVMatchValuesForThisAgentUALib(csvWriterUALib, nextLine[0]);			
		     }
		     reader.close();			
			
//			 while ((userAgentLine = br.readLine()) != null) {
//				// Match object contains results of detection.
//				Match match = provider.match(userAgentLine);
//				checkMatchValues(match);
//				
//				writer.append(userAgentLine);
//			    writer.append(',');
//			    writer.append('\n'); 
//			    writer.append('\n'); 
//			    
//				writeMatchValuesForThisAgent(writer);
//				
//				writer.append("AgentEnd");
//				writer.append('\n');
//				writer.append('\n');			
//			 } 
//			 br.close();
			
		}else{ //We use the user agent defined on this class
			Match match = provider.match(USER_AGENT);
			agent = parser.parse(USER_AGENT);
			
			checkMatchValues(match);
			checkMatchValuesUAlib(agent);
			
//			writer.append(USER_AGENT);
//		    writer.append(',');
//		    writer.append('\n'); 
//		    writer.append('\n'); 
//		    
//			writeMatchValuesForThisAgent(writer);
//			
//			writer.append("AgentEnd");
//			writer.append('\n');
//			writer.append('\n');
			
			//Put all the possible values below				
			CSVWriter csvWriterParams = new CSVWriter(new FileWriter(CSV_FILE_RESULT_PARAMS), '\t');
			CSVWriter csvWriterParamsUALib = new CSVWriter(new FileWriter(CSV_FILE_RESULT_PARAMS_UA), '\t');
			
			for (Map.Entry<String,String[]> entry :match.getResults().entrySet()){
				csvWriterParams.writeNext((entry.getKey() + "#" + StringUtils.join(entry.getValue(),", ")).split("#"));				
//				writer.append(entry.getKey() + " :     " + entry.getValue()[0]);
//				writer.append('\n');
			}			
			
			for (Map.Entry<String,String> entry :valuesMapMaxMind.entrySet()){
				csvWriterParamsUALib.writeNext((entry.getKey() + "#" + entry.getValue()).split("#"));
			}
				
			csvWriterParams.close();
			csvWriterParamsUALib.close();
		}		
		writer.close();
	}		
}