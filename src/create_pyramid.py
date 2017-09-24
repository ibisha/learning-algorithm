import random

# 段数
N = 1000;
# ファイル名
FILE = "test/p67-triangle.txt"

with open(FILE, "w") as f:
  for i in range(1, N + 1):
    line = ""
    for j in range(0, i):
      line += str(random.randint(10, 99)) + " "

    line = line[:-1]
    f.write(line + "\n")
