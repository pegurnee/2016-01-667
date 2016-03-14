data = [
    (6.2,70,'high'),
    (5.3,120,'low'),
    (5.5,90,'low'),
    (4.2,105,'high'),
    (4.5,100,'high'),
    (7.0,110,'low')
  ]

def vari(_a):
  _tot = 0
  _mean = mean(_a)
  for _i in _a:
    _subbed = _i - _mean
    _exp = _subbed ** 2
    _tot += _exp
  _vari = _tot / len(_a)
  return _vari

def part_by_class(data):
  
