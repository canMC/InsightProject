#pip3 install kafka-python
#from kafka import KafkaClient
#from kafka import SimpleProducer
#KAFKA_NODE="ec2-52-38-52-141.us-west-2.compute.amazonaws.com:9092"
#cluster = kafka.KafkaClient("ec2-52-38-52-141.us-west-2.compute.amazonaws.com:9092")
#producer = SimpleProducer(cluster, async=False)
#producer.send_messages('my-topic', b"test")

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

KAFKA_NODE="ec2-52-38-52-141.us-west-2.compute.amazonaws.com"
KAFKA_TOPIC="expedia"
DATADIR="/home/ubuntu/data/"

#open input file for reading the payment strings
data = open("/home/ubuntu/insight-project/data/expedia_synthesized.json", "r")
line = next(data)
for num, nextline in enumerate(data):
    if random.randrange(num + 2): continue
    line = nextline
data.close()

    
#Create producer and send message
#client = KafkaClient(KAFKA_NODE)
cluster = SimpleClient("ec2-52-10-62-22.us-west-2.compute.amazonaws.com:9092")
producer = SimpleProducer(cluster, async=False)
producer.send_messages('expedia-topic', line)
