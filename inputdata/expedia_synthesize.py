import random
import time

LOCATIONS = [("San Francisco", "SFO"), ("Seatle", "SEA"), ("Los Angeles", "LAX"), ("Toronto", "YYZ"), ("New York", "JFK"), ("Boston", "BOS"), ("Dallas", "DAL"), ("Chicago", "ORD"), ("Vancouver", "YVR"), ("Oakland", "OAK"), ("San Jose", "SJC"), ("Denver", "DEN"), ("Washington", "WAS"), ("Miami", "MIA"), ("Orlando", "ORL"), ("Fort Lauderdale", "FLL"), ("Albany", "ALB")]

AIRLINES = ["United", "Air Canada", "Jazz", "American Airline", "Sothwest", "Air Transat", "Delta"]

file = open("expedia_synthesized.json", "a")

i = 0
while i < 5000:
    i = i+1
    f = random.randrange(len(LOCATIONS))
    location_from = LOCATIONS[f][0]
    code_from = LOCATIONS[f][1]
    t = random.randrange(len(LOCATIONS))
    while f == t: t = random.randrange(len(LOCATIONS))
    location_to = LOCATIONS[t][0]
    code_to = LOCATIONS[t][1]

    a = random.randrange(len(AIRLINES))
    air = AIRLINES[a]

    """Get a time at a proportion of a range of two formatted times.
    
    start and end should be strings specifying times formated in the
    given format (strftime-style), giving an interval [start, end].
    prop specifies how a proportion of the interval to be taken after
    start.  The returned time will be in the specified format.
    """
    format = '%m/%d/%Y %I:%M %p'
    prop = random.random()
    stime = time.mktime(time.strptime("10/1/2016 7:00 AM", format))
    etime = time.mktime(time.strptime("10/15/2016 11:00 PM", format))
    ptime_d = stime + prop * (etime - stime)
    departureTimeRaw = time.strftime(format, time.localtime(ptime_d))
    #print (departureTimeRaw)
    ptime_a = ptime_d + random.randint(10000,20000)
    arrivalTimeRaw = time.strftime(format, time.localtime(ptime_a))
    #print (arrivalTimeRaw)

    totalFare = random.uniform(100, 1100)

    json_str = '{"departureAirportLocation": "' + location_from + '", "departureAirportCode": "' + code_from + '", "arrivalAirportLocation":"' + location_to + '", "arrivalAirportCode": "' + code_to + '", "airlineName": "' + air + '", "departureTimeRaw": "' + str(departureTimeRaw) + '", "arrivalTimeRaw": "' + str(arrivalTimeRaw) + '", "totalFare": "' + "%.2f" % totalFare + '"}' + '\n'
    file.write(json_str)

file.close()