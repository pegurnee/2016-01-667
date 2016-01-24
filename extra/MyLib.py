def minimum(arr):
  _min = arr.pop()
  for _elem in arr:
    if _min > _elem:
      _min = _elem

  return _min

def maximum(arr):
  _max = arr.pop()
  for _elem in arr:
    if _max < _elem:
      _max = _elem

  return _max

def mean(arr):
  pass

def median(arr):
  pass

def mode(arr):
  pass

def stdev(arr):
  pass

def sum(arr):
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
  print(min([1,2,5,-3,7,3.0]))
  # main()
