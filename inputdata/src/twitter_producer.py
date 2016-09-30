#pip3 install kafka-python
#from kafka import KafkaClient
#from kafka import SimpleProducer
import sys
import re
import os
import datetime
import json
import time
import glob
import random
 
from kafka import SimpleClient
from kafka import SimpleProducer

#KAFKA_NODE="ec2-52-38-52-141.us-west-2.compute.amazonaws.com"
#KAFKA_TOPIC="twitter"
#DATADIR="/home/ubuntu/data/"

cluster = SimpleClient("ec2-52-10-62-22.us-west-2.compute.amazonaws.com:9092")
producer = SimpleProducer(cluster, async=False)

#open input file for reading the payment strings
data = open("/home/ubuntu/insight-project/inputdata/data/Twitter_all_filtered.json", "r")
line = next(data)
for line in data:
 #   if random.randrange(num + 2): continue
 #   line = nextline
    producer.send_messages('twitter-topic', line)
data.close()

