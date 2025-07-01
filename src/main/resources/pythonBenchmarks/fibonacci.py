i = 1
a = 0
b = 1

while i <= 40:
    temp = b
    b = a + b
    a = temp
    print(b, end=" ")
    i = i + 1
