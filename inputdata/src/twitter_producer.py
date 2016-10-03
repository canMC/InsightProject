import sys
import re
import os

from readconfig import config
 
from kafka import SimpleClient
from kafka import SimpleProducer


cluster = SimpleClient(config["kafka.producer"])
producer = SimpleProducer(cluster, async=False)

#open input file for reading tweets
data = open("/home/ubuntu/insight-project/inputdata/data/Twitter.json", "r")
line = next(data)
for line in data:
    producer.send_messages('twitter-topic', line)
data.close()

