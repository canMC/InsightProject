#!/usr/bin/env python
import redis
from app import app

app.pool = redis.ConnectionPool(host="172.31.1.44", port=6379)

app.run(host='0.0.0.0', debug = True)


