_sum = 20+10+5+9+14+25+15+9
_len = len('20+10+5+9+14+25+15+9'.split('+'))
_mean = _sum / _len

'20+10+5+9+14+25+15+9'.replace('+',',')
a = [20,10,5,9,14,25,15,9]

_tot = 0
for _i in a:
  _subbed = _i - _mean
  print('(%s - %s) = %s' % (_i, _mean, _subbed))

  _exp = _subbed ** 2
  print('  %s ^ 2 = %s' % (_exp, _subbed))

  _tot += _exp

print('E =>%s' % _tot)
print('  ')



(20-13.375)
(10-13.375)
(5-13.375)
(9-13.375)
(14-13.375)
(25-13.375)
(15-13.375)
(9-13.375)
