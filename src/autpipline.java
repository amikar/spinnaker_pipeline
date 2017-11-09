import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.*;
import java.io.*;
import java.util.*;



public class autpipline {
    public static void main(String[] args) throws Exception, JSONException, IOException{
    	
    	
    	 // Get name of applications //
    	
    	getappname();
    	
    	
    	// Get name and status of pipelines for given app //
    	
		String appname;
    	System.out.println ("Please enter the application to see pipeline status");
    	Scanner appnamef = new Scanner(System.in);
    	appname = appnamef.nextLine();
	
    	
    	URL newconnection = new URL("http://localhost:8084//applications/"+ appname + "/pipelines");
    	
    	
    	
        URLConnection yc = newconnection.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        
        while ((inputLine = in.readLine()) != null) 
        	sb.append(inputLine); 
    		

    	JSONArray json = new JSONArray(sb.toString());
    
    	String status = "";
    	String pipe = "";
  		Set<String> a = new TreeSet<String>();
  		
  		String pipetw = "";
  		Set<String> b = new TreeSet<String>();
  		
    	int n = json.length();
   		for (int i = 0; i < n; ++i) {
   		
   		  	JSONObject pipename = json.getJSONObject(i);
     		pipe = pipename.getString("name");
			a.add(pipe);
			
			pipetw = pipename.getString("status");
			b.add(pipetw);
			
            status = pipename.get("status").toString();
            String name = pipename.get("name").toString();
  
			if (status.equals("TERMINAL")) {
                System.out.println("Pipeline Name: " + name + "\t" + "Status: " + status);
            }	
            if (status.equals("RUNNING")) {
                System.out.println("Pipeline Name: " + name + "\t" + "Status: " + status);
            }			
            if (status.equals("SUCCEEDED")) {
                System.out.println("Pipeline Name: " + name + "\t" + "Status: " + status);
            }	
            if (status.equals("CANCELED")) {
                System.out.println("Pipeline Name: " + name + "\t" + "Status: " + status);
            }	
			
		 if (status.equals("RUNNING")) {
                System.out.println("All running pipeline are :  " + name + "\t" );
            }	
									
		}
		System.out.println("The pipelines present for the given application are : "+ a);
		
		

				
		
		
		// find deploy stage //
		
		
		System.out.println("To get the deploy stage of a pipeline , enter name of pipeline");
		Scanner pipeinput = new Scanner(System.in);
		String pipechoice = pipeinput.nextLine();
		
		String getstage;
		StringBuilder stage = new StringBuilder();
		
		
		URL conforpipe = new URL("http://localhost:8084//applications/"+appname +"/pipelineConfigs/"+pipechoice );
		URLConnection stagename = conforpipe.openConnection();
        BufferedReader forstages = new BufferedReader(new InputStreamReader(stagename.getInputStream()));
        while ((getstage = forstages.readLine()) != null) 
        	stage.append(getstage); 
        	
     	JSONObject jsonforstages = new JSONObject(stage.toString());
    	JSONArray stagesjson = jsonforstages.getJSONArray("stages");
    	
    
    
    
    //find stages//
    
    	
    
    	String stagetype = "";
    	Set<String> findstagetype = new TreeSet<String>();
    	JSONArray findstageclusters = new JSONArray();

    	
    	int stagejsonlength = stagesjson.length();
   		for (int i = 0; i < stagejsonlength; ++i) {
   			
     		JSONObject stagenamejson = stagesjson.getJSONObject(i);
    
     		stagetype = stagenamejson.getString("type");
 			
			findstagetype.add(stagetype);
			
			if(stagetype.equals("deploy"))
			{
			
			
			
			//update the json file to include aca stage//
			
			
			
			String s =  "{\"baseline\":{},\"canary\":{\"application\":\"awsgridapp\",\"canaryConfig\":{\"canaryAnalysisConfig\":{\"lookbackMins\":0,\"notificationHours\":[],\"useGlobalDataset\":false,\"useLookback\":false},\"canaryHealthCheckHandler\":{\"@class\":\"com.netflix.spinnaker.mine.CanaryResultHealthCheckHandler\"},\"name\":\"gridpipeline - Canary\"},\"canaryDeployments\":[{\"@class\":\".CanaryTaskDeployment\",\"accountName\":null,\"type\":\"query\"}],\"owner\":null,\"watchers\":[]},\"name\":\"ACA Task\",\"refId\":\"5\",\"requisiteStageRefIds\":[\"4\"],\"type\":\"acaTask\"}";
			
			System.out.println("Enter the aca stage json tempelate as string");
			System.out.println("for testing purpose enter the string below");
			System.out.println(s);
			Scanner sinput = new Scanner(System.in);
			String schoice = sinput.nextLine();
			
			
			
				schoice= schoice.replaceAll("\\n", "\n");
       			JSONObject addst = new JSONObject(schoice); 
    	
    	
    			stagesjson.put(addst);
    
				jsonforstages.remove("stages");
    		
    			jsonforstages.put("stages",stagesjson);
    			
    			
    			
					if(stagenamejson.has("clusters"))
           			 {
          				  JSONArray stratjson = stagenamejson.getJSONArray("clusters");
          				  findstageclusters.put(stratjson);

            }
			
				
			}
			
    	}
    if(findstagetype.contains("deploy"))
    	
    	{
    		System.out.println("Deploy stage is present");
  
    	}
    else {System.out.println("Deploy stage is not present");}
    
    
    	
    System.out.println("The name of stages present in the pipeline are : ");
    System.out.println(findstagetype);
    
    String pipestrategy = findstageclusters.getJSONArray(0).getJSONObject(0).getString("strategy");
    	
   	 	
    System.out.println("Name of the strategy used for deploy stage is : ");
    System.out.println(pipestrategy);
   
   
   
   
   
   
   //add aca stage//
   
   
 		 if (!status.equals("RUNNING")) {
   
 		System.out.println("Enter 'yes' if you want to add the ACA stage");
		Scanner addacastage = new Scanner(System.in);
		String addaca = addacastage.nextLine();
   
		String fstage = jsonforstages.toString();
   
     
     
  if(addaca.equals("yes") && (pipestrategy.equals("highlander")||pipestrategy.equals("redblack"))) 
  
  {
	  
	  System.out.println("Adding the ACA stage");

 
	  String urly = "http://localhost:8084/pipelines";
	  URL obj = new URL(urly);
	  HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        // Setting basic post request
  
  
	  con.setRequestMethod("POST");
	  con.setRequestProperty("Content-Type","application/json");
 
  
  // Send post request
  
  
	  con.setDoOutput(true);
	  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	  wr.writeBytes(fstage);
	  wr.flush();
	  wr.close();
 
	  int responseCode = con.getResponseCode();
	  System.out.println("Response Code : " + responseCode);
 
	  BufferedReader iny = new BufferedReader(
	  new InputStreamReader(con.getInputStream()));
	  String output;
	  StringBuffer response = new StringBuffer();
 
	  while ((output = iny.readLine()) != null)
	  {
		  response.append(output);
	  }
	  	iny.close();
  
	  	//printing result from response
	  		System.out.println(response.toString());
  		}
  
  		else
  		{
  			System.out.println("Exiting program without adding ACA stage");
  		}   

  			addacastage.close();

 		 }
 		 else {
 			 System.out.println("A pipeline might be running or your pipeline configuration strategy is not supported");
 		 }

    }
    
		
		
// Gets name of applications//	


static void  getappname() throws Exception, JSONException{
		String getappname;
    	StringBuilder app = new StringBuilder();
    	
    	URL appsname = new URL("http://localhost:8084//applications/");
    	URLConnection hj = appsname.openConnection();
        BufferedReader ne = new BufferedReader(new InputStreamReader(hj.getInputStream()));
        while ((getappname = ne.readLine()) != null) 
        	app.append(getappname); 
        	
     	JSONArray jsonapp = new JSONArray(app.toString());
    	String appnamefinal = "";
  		Set<String> finalapp = new TreeSet<String>();
    	
    	int appjsonlength = jsonapp.length();
   		for (int i = 0; i < appjsonlength; ++i) {
   		
   		  	JSONObject appnamejson = jsonapp.getJSONObject(i);
     		appnamefinal = appnamejson.getString("name");
			finalapp.add(appnamefinal);
			
    	}
    	System.out.println("The applications present are : "+ finalapp);


		}
		
		
}
		
		
	
	

    
        
   
