def matching(_a, _b):
  _count = 0
  for _i in range(len(_a)):
    if _a[_i] == _b[_i]:
      _count += 1
  return _count / len(_a), '%s/%s' % (_count, len(_a))

def jacard(_a, _b):
  _tot = 0
  _count = 0
  for _i in range(len(_a)):
    if _a[_i] or _b[_i]:
      _tot += 1
      if _a[_i] == _b[_i]:
        _count += 1
  return _count / _tot, '%s/%s' % (_count, _tot)

a = [0,0,1,0,1,1,0,0,0,1]
b = [1,0,0,0,1,1,0,0,0,1]

print(matching(a, b))
print(jacard(a, b))
