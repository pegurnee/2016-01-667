from collections import Counter

records = [
  (0, 1, 0, 2),
  (1, 0, 1, 3),
  (1, 1, 0, 2),
  (1, 1, 1, 2),
  (0, 0, 0, 1)
]

def gini(arr):
  return 1 - sum([(x / sum(arr)) ** 2 for x in arr])

def class_entropy(loc):
  _sum = 0
  for recs in group_by(loc):
    _sum += gini(recs.values()) * sum(recs.values()) / len(records)
  # return gini(arr), len(arr)
  return _sum

def group_by(loc):
  left  = []
  right = []
  for rec in records:
    if rec[loc]:
      right.append(rec[-1])
    else:
      left.append(rec[-1])
  return Counter(left), Counter(right)

if __name__ == '__main__':
  # for x in group_by(0):
  #   print(x.values())
  #   print(gini(x.values()))

  for i in range(3):
    print([gini(x.values()) for x in group_by(i)])
    print(class_entropy(i))
