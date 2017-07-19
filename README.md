# FIESTA-IoT Experiment Result Store  

## Prerequisites  
 - Java JDK 1.8  
 - Maven  
 - MySQL Server Community Edition 5.x  
 - Apache Tomcat 7/8 or Wildfly 10  

## Configuration  

### Database Setup  

A schema and table need to be created in the MySQL database. The following SQL script can be used to create it:

``` sql  
CREATE SCHEMA IF NOT EXISTS fiesta_iot_ers;

CREATE TABLE IF NOT EXISTS fiesta_iot_ers.experiments (
    USER_ID varchar(255),
    FEMO_ID varchar(255),
    JOB_ID varchar(255),
    TIME_STAMP varchar(255),
    EXPR_RESULT MEDIUMTEXT
);
```  

![experiment store example - header](https://www.dropbox.com/s/tc5kcr56c9i0mo4/store-expr-example-db.png?dl=1)  

The script can be found under the folder ***/WEB-INF/sql-scripts/create-expr-store.sql***.  

### Code Properties  

There are two Properties files under ***/WEB-INF/config***. db.properties is dedicated for database connection setting, and the other is for global properties (note this file will be substituted with the common properties file for the core platform.  

#### db.properties  

``` 
#url for database
eee.ers.db.username=jdbc:mysql://localhost:3306/fiesta_iot_ers

#username for database
eee.ers.db.username=fiesta

#password for database
eee.ers.db.password=fiesta

``` 
#### global.properties  

```
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

hostname = {hostname}/openam/json/users?_action=idFromSession

``` 
### Deploy  
Once all these have been set then the generated WAR file can be deployed on the servlet container.  

## Web API  

The ERS mainly allows experiment results to be stored and retrieved. 

The URL used to invoke the service is:
```  
https://{hostname}/experiment-result-store  
```   
 Results for a particular user can be stored and obtained by setting certain fields in the HTTP header.

| Header  | Description  |
|---|---| 
|userId|  User ID for the experimenter. This is the same as the OpenAM username. |
|femoId|  FEMO ID for the experiment. |
|jobId| Job ID for the experiment process.|
|iPlanetDirectoryPro| Open AM token for the corresponding user. This will be used to identify the user. |
|Content-type| The content type for the body when storing. This is always set to **application/json**.|
|Accept| The accept type for the body when retrieving. This is always set to **application/json**.|

### storeExprResult()  
This method allows experiment results to be stored in persistence until it is retrieved by the experimenter.  
``` 
POST /experiment-result-store HTTP/1.1
Host: {hostname}
userId: {userId}
femoId: {femoId}
jobId: {jobId}
Content-Type: application/json
```  

The body of the request must be in the format below:

```json  
{"Result":""}
```  
 Where **Result** will hold the value of the experiment job result.
 
### getExprResults()  

The method allows experiment results to be retrieved when an experimenter logs in to check on the status of the experiment result.  

 - If a FEMO ID and Job ID is provided, then the corresponding experiment result is returned. 
 - If only the FEMO ID is provided, then all experiment results under all it corresponding job IDs will be returned. 

Currently, once the requested results are retrieved from the database, it's corresponding entry in the database will be deleted and cannot be retrieved again. 

*N.B. A future release will allow a expiration time once the data has been requested for the first time.*
``` 
GET /experiment-result-store HTTP/1.1
Host: {hostname}
iPlanetDirectoryPro: {openAM_token}
femoId: {femoId}
jobId: {jobId}
Accept: application/json 
```  

The body of the request must be in the format below:

```json  
{
    "femoResults": [
        {
            "jobid": "103",
            "results": [
                {
                    "time": "2017-05-24T19:30:02Z",
                    "result": "hi"
                }
            ]
        },
        {
            "jobid": "900",
            "results": [
                {
                    "time": "2017-06-06T18:46:52Z",
                    "result": ",http://smart-ics.ee.surrey.ac.uk/fiesta-iot/resource/sc-sics-sp-002-power,http://smart-ics.ee.surrey.ac.uk/fiesta-iot/resource/sc-sics-sp-001-power\n0.0,1.432187701766452e-14,1.9040324872321435e-14\n0.00819672131148,10.483904864244515,12.66849135485158\n0.016393442623,10.037536235817262,11.694920662095793\n0.0245901639344,8.274362461842944,10.787303593017295\n0.0327868852459,6.936821514862263,8.95507559616293\n0.0409836065574,5.322912117131182,6.684190365119811\n0.0491803278689,4.0828434819081725,4.156983856615794\n0.0573770491803,2.267728032490481,2.530561320212919\n0.0655737704918,0.6565507982225585,0.6050609198606894\n0.0737704918033,1.3997255070742531,1.0799031869904239\n0.0819672131148,1.665701660453452,2.954284570154875\n0.0901639344262,1.6320456294790227,2.5005243782136066\n0.0983606557377,1.5195656197778484,2.7019058846634727\n"
                }
            ]
        }
    ]
}
```  

### Example  usage

#### Storing a result  
![experiment store example - header](https://www.dropbox.com/s/ujjbz07so0cwwkt/store-expr-example-header.png?dl=1)  

![experiment store example - body](https://www.dropbox.com/s/7c6mckriyp8fogk/store-expr-example-body.png?dl=1)  

#### Retrieving a result  
![experiment store example - body](https://www.dropbox.com/s/bea2ify1uguxxrn/retreive-expr-example-header.png?dl=1)  

![experiment store example - body](https://www.dropbox.com/s/fpnfmga82ct3h90/retreive-expr-example-body.png?dl=1)  


### TODO
getExprResultList() 
getExprResult() 
deleteExprResults()  
