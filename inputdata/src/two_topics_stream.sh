#!/bin/bash
set -eu

COUNTER=0
while [  $COUNTER -lt 500 ]; do
python twitter_producer.py
python expedia_producer.py
let COUNTER=COUNTER+1
done
#sleep 1m
