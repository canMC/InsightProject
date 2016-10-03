# Ready To Fly: a real-time streaming join.

Ready To Fly is an app that lets you to match Twitter users expressed their interest in vacation with the current flight deals in Expedia in real-time.

This project was inspired by the following excellent research paper on streaming join: 
http://research.google.com/pubs/pub41318.html 
Photon: Fault-tolerant and Scalable Joining of Continuous Data Streams. SIGMOD '13: Proceedings of the 2013 international conference on Management of data, ACM, New York, NY, USA, pp. 577-588

I completed this project as a Fellow in the 2016C Insight Data Engineering Silicon Valley program.

The typical use case for a streaming join system involves businesses that want to relate web search queries and user clicks on advertisements. Such system produces joined logs that are used to derive key business metrics, including billing for advertisers. Another usefull example involves bid match services or fraud prevention systems where the real time response is essential.

Ready To Fly app is powered by:

- Apache Kafka
- Apache Flink
- Redis
- Flask


## Demo

A video of the project in action can be seen [here](http://readytofly.top/deal).


## Pipeline

All of the techonlogies listed above are connected together as follows.

![Pipeline](http://imgur.com/6cAJIfm.png)

The key layers of the system are:

- Real-time ingestion via Kafka from streaming sources (Twitter and Expedia)
- Flink cluster to distribute and process tweets and flight deals from Kafka
- Redis server which receives matches from Flink and delivers them to the application server
- Application server (Python Flask) which displays the joined results in a table format


## How It Works

Ready To Fly reads in user tweets and flight deals as JSON messages using Kafka. These are processed and joined in real-time using Apache Flink, and stored in a Redis database. 

## Data Synthesis

Expedia deals are simulated using a set of airport locations and randomly assigned dates (within one month period) and prices. A random sample of 6,000 deals is selected.

Tweets for the demo are synthesized using a historical set of Twitter tweets extracted from the API which matches the list of vacation keywords. Due to low volume of tweets available through public API with the real physical location specified, one tweet is selected from the file ("Twitter.json") and sent with randomly assigned location from the selected set of locations.


## Real-time Processing

Real-time processing is handled by Flink.  Flink allows to impose windows upon the stream to do mini-calculations before persisting data which reduces the load on the database.  A window segments your data by time (processing time, ingestion time, or event time) which waits until the specified moment to run the calculation on the rolling stream.  

Two real time streams are set up. One reads in flight deals from Expedia, and another reads in tweets from Twitter. Relevant fields are distributed among partitions and joined by Flink based on location as a key. After the join Flink passes the results to Redis for short-term persistence.


## Databases

Redis is used to store the result data before it's displayed by the front end. 