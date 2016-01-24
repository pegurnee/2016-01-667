def min(arr):
  _min = arr.pop()
  for _elem in arr:
    if _min > _elem:
      _min = _elem

  return _min


if __name__ == '__main__':
  print(min([1,2,5,-3,7,3.0]))
  # main()
