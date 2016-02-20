import math

def gini(arr):
  _vals = [(x / sum(arr)) ** 2 for x in arr]
  return 1 - sum(_vals)

def logb(val):
  return math.log(val, 2)

def shannon(arr):
  tot = sum(arr)
  _vals = [(x / tot * logb(x / tot)) for x in arr]
  return -sum(_vals)

if __name__ == '__main__':
  a = [6, 10, 4]

  _tot = sum(a)
  _vals = [(x / _tot) ** 2 for x in a]
  print(_vals)
  print(sum(_vals))

  print(gini(a))

  _vals = [(x / _tot * logb(x / _tot)) for x in a]
  print(_vals)

  print(shannon(a))

  g1 = gini([5, 5, 5])
  g2 = gini([6, 5, 4])
  g3 = gini([10, 3, 2])
  ginis = [g1, g2, g3]

  print(ginis)
  print(max(ginis))
