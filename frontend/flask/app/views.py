from flask import render_template, request
from flask import jsonify 
from app import app
import itertools
from flask import Flask, Response
from redis import Redis
import json
import ast

@app.route('/_fetch_messages')
def fetch_messages():
	# get a redis connection
    	r = Redis(host='172.31.1.44', port=6379, db=0)
 
        matches = r.lrange("dealsForUsers",0,100)
	ll = []
	
	if len(matches)>0:
	  ll.append(matches[0])
	length = len(matches)
	while length > 0:
          r.lpop("dealsForUsers")
	  length = length-1

	return jsonify(result=ll)

@app.route('/deal')
def deal():
	return render_template("deal.html")

@app.route("/deal", methods=['POST'])
def deal_post():

	bDeal = True

	return render_template("deal.html", bDeal=bDeal)

    # if clear button pressed:
	#TODO

