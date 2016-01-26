values = '20+10+5+9+14+25+15+9'

a = [20,10,5,9,14,25,15,9]
_sum = sum(a)
_len = len(a)
_mean = _sum / _len

_str = []
_tot = 0
for _i in a:
  _subbed = _i - _mean
  print('(%s - %s) = %s' % (_i, _mean, _subbed))
  _exp = _subbed ** 2
  print('  %s ^ 2 = %s' % (_subbed, _exp))
  _tot += _exp
  _str.append('((%s - %s) ^ 2)' % (_i, _mean))

print(' + '.join(_str))

print('E =>%s' % _tot)
_len = len(a)
_vari = _tot / _len
print('  %s / %s = %s' % (_tot, _len, _vari))
_stdev = _vari ** 0.5
print('  sqrt(%s) = %s' % (_vari, _stdev))
print('stdev => %s' % _stdev)

a.sort()
print('sorted:\n  %s' % a)
if _len % 2 == 0:
  _median = (a[int(_len / 2) - 1] + a[int(_len / 2)]) / 2
else:
  _median = a[int(_len / 2)]

print('median = %s' % _median)
