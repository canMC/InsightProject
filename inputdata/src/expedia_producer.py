import sys
import re
import os
import random

from readconfig import config

from kafka import SimpleClient
from kafka import SimpleProducer

cluster = SimpleClient(config["kafka.producer"])
producer = SimpleProducer(cluster, async=False)

#open input file for reading expedia deals
data = open("/home/ubuntu/insight-project/inputdata/data/expedia_synthesized.json", "r")
line = next(data)
for num, nextline in enumerate(data):
    if random.randrange(num + 2): continue
    line = nextline
    producer.send_messages('expedia-topic', line)
data.close()

