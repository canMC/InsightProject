#!/bin/bash
set -eu

COUNTER=0
while [  $COUNTER -lt 100 ]; do
python twitter_producer.py
let COUNTER=COUNTER+1
done
#sleep 1m
