def stdev(_a):
  _tot = 0
  for _i in _a:
    _subbed = _i - _mean
    _exp = _subbed ** 2
    _tot += _exp
  _vari = _tot / len(_a)
  return _vari ** 0.5

def mean(_a):
  _sum = sum(_a)
  _len = len(_a)
  return _sum / _len

a = [ 20, 30, 40, 10, 50, 20, 30, 60, 90]
b = [  3,  5,  4,  1,  6,  2,  4,  8, 10]

_mA = mean(a)
_mB = mean(b)
_covarSum = 0

for _i in range(len(a)):
  _covarSum += (a[_i] - _mA) * (b[_i] - _mB)

_covar = _covarSum / len(b)

_correl = _covar / (stdev(a) * stdev(b))

print(_correl)
