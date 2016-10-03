import sys
import re

with open("../../config/config.properties", "r") as f:
  lines = f.readlines()
    
config={}
for line in lines:
  if line.find("=")!=-1:
    ls = line.split("=")
    config[ls[0]]=ls[1]

