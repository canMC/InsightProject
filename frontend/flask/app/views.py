from flask import render_template, request
from flask import jsonify 
from app import app
import itertools
from flask import Flask, Response
#from redis import Redis


@app.route('/email')
def email():
 return render_template("email.html")

@app.route("/email", methods=['POST'])
def email_post():
    jsonresponse = {"name": "Milena", "text": "Tired"}
    return render_template("emailop.html", output=jsonresponse)
    
    
    # if clear button pressed:
    if 'clear' in request.form:
        return render_template("mybase.html")



#emailid = request.form["emailid"]
#jsonresponse = [{"name": "Milena", "text": "Tired"}]
#return render_template("mybase.html", output=jsonresponse)

#@app.route('/_fetch_messages')
#def fetch_messages():
        # get a redis connection
        #r = redis.StrictRedis(host='172.31.1.44', port=6379, db=0)
        #r = redis.StrictRedis(host='localhost', port=6379, db=0)
        #p = r.pubsub()
        #p.subscribe('dealsForUsers')
        #p.get_message()
        #    jsonresponse = [{"name": "Milena", "text": "Tired"}]
#    return jsonify(result=jsonresponse)
#render_template("mybase.html", output=jsonresponse)
