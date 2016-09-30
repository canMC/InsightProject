#!/bin/bash
set -eu

COUNTER=0
while [  $COUNTER -lt 10000 ]; do
python expedia_producer.py
let COUNTER=COUNTER+1
done
#sleep 1m
