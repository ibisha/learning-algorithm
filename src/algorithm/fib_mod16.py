
# メモ領域
cache = [0, 1]

def fib(n):
    # 16の剰余にした場合、24周期で同じ値を繰り返す
    n = n % 24
    if len(cache) <= n:
        cache.append((fib(n - 1) + fib(n - 2)) % 16)

    return cache[n]


try:
    while True:
        n = input().strip().upper()
        print(fib(int(n)))
except EOFError:
    pass
