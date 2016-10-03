from twython import Twython
import urllib3.contrib.pyopenssl
urllib3.contrib.pyopenssl.inject_into_urllib3()
import json, random

LOCATIONS = ["San Francisco", "Seatle", "Los Angeles", "Toronto", "New York", "Boston", "Dallas", "Chicago", "Vancouver", "Oakland", "San Jose", "Denver", "Richmond", "Washington", "Miami", "Orlando", "Fort Lauderdale", "Albany"]

#poll twitter to get a historical set of tweets for synthetic tweet generation
#TODO add more search keywords
KEYWORDS = ["vacation", "San Francisco", "Seatle", "Los Angeles", "Toronto", "New York", "Boston", "Dallas", "Chicago", "Vancouver", "Oakland", "San Jose", "Denver", "Richmond", "Washington", "Miami", "Orlando", "Fort Lauderdale", "Albany","beach", "holiday"]

keyfile = open("keys.txt", "r")
TWITTERKEYS = dict(line.rstrip().split(": ") for line in keyfile)
keyfile.close()

t = Twython(TWITTERKEYS['consumer key'], TWITTERKEYS['consumer secret'], oauth_version=2)
ACCESS_TOKEN = t.obtain_access_token()
t = Twython(TWITTERKEYS['consumer key'], access_token=ACCESS_TOKEN)

filedump = open("Twitter_dump.json", "a")
for k in KEYWORDS:
    file = open("Twitter.json", "a")
    results = t.search(q=k, lang='eu', count=100)
    filedump.write(json.dumps(results, ensure_ascii=False))
    for res in results['statuses']:
        if res['text'] and res['user']['location'] and res['user']['name']:
            text = res['text'].replace('\n', ' ')
            name = res['user']['name']
            location = LOCATIONS[random.randrange(len(LOCATIONS))]
            json_str = '{"location":"' + location + '", "name":"' + name + '", "text":"' + text + '"}' + '\n'
            file.write(json_str)
            
            i = 0
            while i<10:
                location = "none"+str(i)
                json_str = '{"location":"' + location + '", "name":"' + name + '", "text":"' + text + '"}' + '\n'
                file.write(json_str)
                i=i+1


file.close
filedump.close


