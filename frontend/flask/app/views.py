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

      #  matches = r.lpop("dealsForUsers")
        matches = r.lrange("dealsForUsers",0,100)
	ll = []
	
	if len(matches)>0:
	  ll.append(matches[0])
	length = len(matches)
	while length > 0:
          r.lpop("dealsForUsers")
	  length = length-1

	return jsonify(result=ll)

@app.route('/email')
def email():
 return render_template("email.html")

@app.route("/email", methods=['POST'])
def email_post():

    bDeal = True
    #jsonresponse = {"name": "Milena", "text": "Tired"}
    return render_template("email.html", bDeal=bDeal)

    # if clear button pressed:
	#TODO

