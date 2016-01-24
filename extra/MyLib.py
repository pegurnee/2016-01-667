def minimum(arr):
  _min = arr[0]
  for _elem in arr:
    if _min > _elem:
      _min = _elem

  return _min

def maximum(arr):
  _max = arr[0]

  for _elem in arr:
    if _max < _elem:
      _max = _elem

  return _max

def mean(arr):
  return sum(arr) / len(arr)

def sum(arr):
  _total = 0

  for _elem in arr:
    _total += _elem

  return _total

def median(arr):
  _len = len(arr)
  _arr = sort(arr)

  if _len % 2 == 0:
    _ret = mean(_arr[int(_len / 2) - 1 : int(_len / 2) + 1])
  else:
    _ret = _arr[int(_len / 2)]

  return _ret

def sort(arr):
  return sorted(arr)

def mode(arr):
  _listing = {}

  for _elem in arr:
    if _elem in _listing:
      _listing[_elem] += 1
    else:
      _listing[_elem] = 1

  sorted(_listing, key=lambda _item: )
  print(_listing)
  print(sorted(_listing))

def stdev(arr):
  pass

def percentile(arr, percent):
  pass

def correlation(arrU, arrV):
  pass

def euclidean(arrU, arrV):
  pass

def manhattan(arrU, arrV):
  pass

def cosine(arrU, arrV):
  pass

def matching(arrU, arrV):
  pass

def jacard(arrU, arrV):
  pass


if __name__ == '__main__':
  a =[2.5,5,-3,1.1,1,7,3,2.5]
  print(sorted(a))
  print('min = %s' % minimum(a))
  print('max = %s' % maximum(a))
  print('med = %s' % median(a))
  print('avg = %s' % mean(a))
  mode(a)
  # main()
