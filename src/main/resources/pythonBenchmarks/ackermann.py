import sys
sys.setrecursionlimit(9000)
def ackermann(m, n):
    if m == 0:
        return n + 1
    if n == 0:
        return ackermann(m - 1, 1)
    return ackermann(m - 1, ackermann(m, n - 1))

# Example usage:
result = ackermann(3, 10)
print(result)