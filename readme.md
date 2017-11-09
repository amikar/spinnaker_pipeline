Spinnaker pipeline, script to check running pipelines for an application and add or remove stages based on json. is made to check for different stages of the pipeline , check if it is running or paused etc

Spinnaker: Documentation for adding ACA stage to the pipeline

 
Note: It is assumed you have the spinnaker instance installed and running on the local system to perform the operations offered by the program. 

Getting applications present : 

For getting the names of Application’s present in spinnaker. The program makes a call to url 
“http://localhost:8084//applications/” This will get us the names of the applications present in spinnaker. 
The program does this by using the call similar to the api method “getAllApplicationsUsingGET” . 


Getting the pipelines and their execution status for a application :  

Input: Application name from the list of applications present.

Once we have the name of the applications present, we are asked to input the application name to get the pipelines present in the application. We will then be presented with the names of the different pipelines present and their status, if the pipeline is terminal, running, cancelled or succeeded in running. 
The program does this by using the call similiar to the api method 
“ getPipelinesUsingGET” . 


Enter the ACA stage JSON to be updated to the pipeline: 

Input: File path for JSON input of ACA stage

Once the pipeline is selected the program asks the ACA stage to be added to the JSON present before. This will be given as an input of JSON’s file path to be added. An example JSON for this ACA stage will be : 









{
		"baseline": {},
		"canary": {
			"application": "awsgridapp",
			"canaryConfig": {
				"canaryAnalysisConfig": {
					"beginCanaryAnalysisAfterMins": "5",
					"canaryAnalysisIntervalMins": "30",
					"lookbackMins": 30,
					"name": "111",
					"notificationHours": [1],
					"useGlobalDataset": false,
					"useLookback": true
				},
				"canaryHealthCheckHandler": {
					"@class": "com.netflix.spinnaker.mine.CanaryResultHealthCheckHandler",
					"minimumCanaryResultScore": "60"
				},
				"canarySuccessCriteria": {
					"canaryResultScore": "85"
				},
				"combinedCanaryResultStrategy": "LOWEST",
				"lifetimeHours": "0.5",
				"name": "gridpipeline - Canary"
			},
			"canaryDeployments": [{
				"@class": ".CanaryTaskDeployment",
				"accountName": "phanip",
				"baseline": "testspinnaker-tspcluster-v017",
				"canary": "testspinnaker-tspcluster-current",
				"type": "query"
			}],
			"owner": "name",
			"watchers": []
		},
		"name": "ACA Stage",
		"refId": "5",
		"requisiteStageRefIds": ["4"],
		"type": "acaTask"
}


Once the ACA stage input is given, the program will check for the types of stages present, if the stage deploy exists and the strategy used is either highlander or redblack, the user is given the option to go ahead and add the ACA stage to the pipeline. 
This is done by using the api method “ updatePipelineUsingPUT “ we are using the post method alteration of this method.
If you see the response code : 200, it means the ACA stage was added successfully. 
The changes can then be seen on the GUI of spinnaker.

Example case : 

Upon running a successful alteration of the program, it should seem something like the result below. 

The inputs by the user are marked in green. The example used is
Application: awsgridapp , pipeline : gridpipeline


------------------------------------------------------------------------------------------------
The applications present are : [awsgridapp, kanaryapp]
Please enter the application to see pipeline status
awsgridapp
Pipeline Name: gridpipeline	Status: TERMINAL
Pipeline Name: gridpipeline	Status: TERMINAL
Pipeline Name: gridpipeline	Status: TERMINAL
The pipelines present for the given application are : [gridpipeline]
To get the deploy stage of a pipeline , enter name of pipeline
gridpipeline
Enter the aca stage json template’s file path
/Users/amikar/Downloads/acajson.json
Deploy stage is present
The name of stages present in the pipeline are : 
[bake, canary, deploy, jenkins]
Name of the strategy used for deploy stage is : 
highlander
Enter 'yes' if you want to add the ACA stage
yes
Adding the ACA stage
Response Code : 200




