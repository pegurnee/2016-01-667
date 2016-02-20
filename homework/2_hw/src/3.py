from collections import Counter

records = []

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
    if rec[loc] in ['college', 'smoker', 'married', 'female', 'retired']:
      right.append(rec[-1])
    else:
      left.append(rec[-1])
  return Counter(left), Counter(right)

if __name__ == '__main__':
  with open('3_data.txt') as f:
    for line in f:
      records.append(line.split())

  for i in range(5):
    print(class_entropy(i))
