#.!/usr/bin/env python
 
import time
 
# read file
rows = []
FILE = "test/p67-triangle.txt"
with open(FILE, "r") as f:
    for blob in f:
        # スペースで分割
        rows.append([int(i) for i in blob.split(" ")])
 
start = time.time()
 
# 底辺から頂点に向かって計算する
for i,j in [(i,j) for i in range(len(rows)-2,-1,-1) for j in range(i+1)]:
    rows[i][j] +=  max([rows[i+1][j],rows[i+1][j+1]])
 
elapsed = time.time() - start
 
print("%s found in %s seconds" % (rows[0][0],elapsed))
