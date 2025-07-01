def smallest_prime_factor(n):
    i = 2
    while i * i <= n:
        if n % i == 0:
            return i
        i = i + 1
    return n

# 35099 * 46147 = 1 619 713 553
ret = smallest_prime_factor(1_619_713_553)
print(ret)

# 65267 * 29243 = 1 908 602 881
ret = smallest_prime_factor(1_908_602_881)
print(ret)

# 22697 * 90359 = 2 050 878 223
ret = smallest_prime_factor(2_050_878_223)
print(ret)
