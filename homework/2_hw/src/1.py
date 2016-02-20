def gini(arr):
  _vals = [(x / sum(arr)) ** 2 for x in arr]
  print(_vals)
  print(sum(_vals))
  return 1 - sum(_vals)


def _entropy(_set):
  _gini = 0

  _n = sum(_set)
  for _s in _set:
    _gini += (_s / _n) ** 2

  return 1 - _gini

if __name__ == '__main__':
  a = [6, 10, 4]

  print(gini(a))
