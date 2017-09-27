
# メモ領域
cache = [0, 1]

def fib(n):
    if len(cache) <= n:
        cache.append((fib(n - 1) + fib(n - 2)) % 16)

    return cache[n]


try:
    while True:
        n = input().strip().upper()
        print(fib(int(n)))
except EOFError:
    pass
