def _abs(_num):
  return -_num if _num < 0 else _num

def euc(_a, _b):
  _sum = 0
  for _i in range(len(_a)):
    _sum += (_a[_i] - _b[_i]) ** 2
  return _sum ** 0.5

def manh(_a, _b):
  _sum = 0
  for _i in range(len(_a)):
    _sum += _abs(_a[_i] - _b[_i])
  return _sum

_s = [
  'blank',
  (5, 9, 2, 8),
  (6, 8, 3, 5),
  (2, 5, 8, 9)
  ]

print(euc(_s[1], _s[2]))
print(manh(_s[2], _s[3]))

print('<%s>' % ('=' * 10))
print(euc(_s[1], _s[2]))
print(euc(_s[2], _s[3]))
print(euc(_s[3], _s[1]))
