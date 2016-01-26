def median(arr):
  _len = len(arr)
  _arr = sorted(arr)
  if _len % 2 == 0:
    _ret = mean(_arr[int(_len / 2) - 1 : int(_len / 2) + 1])
  else:
    _ret = _arr[int(_len / 2)]
  return _ret

def percentile(_arr, _loc):
  _ret = sorted(_arr)
  _len = len(_ret)
  _act_per = _len * _loc / 100
  _idx = int(_act_per)
  if _act_per != float(_idx):
    _idx += 1
  return _ret[_idx - 1]

def five_pts(_arr):
  return min(_arr), percentile(_arr, 25), median(_arr), percentile(_arr, 75), max(_arr)


a = [60,90,80,75,90,70,85]

_maxI = len(a) - 1

print(five_pts(a))
