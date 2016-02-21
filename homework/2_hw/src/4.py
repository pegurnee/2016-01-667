from collections import Counter

def gini(arr):
  return 1 - sum([(x / sum(arr)) ** 2 for x in arr])

def class_entropy(loc, record_set):
  _sum = 0
  for recs in group_by(loc, record_set):
    # print(recs)
    # print(recs.values())
    # print(sum(recs.values()))
    _sum += gini(recs.values()) * sum(recs.values()) / len(record_set)
  # return gini(arr), len(arr)
  return _sum

def group_by(loc, record_set):
  left  = []
  right = []
  for rec in record_set:
    if rec[loc] in ['college', 'smoker', 'married', 'female', 'retired']:
      right.append(rec[-1])
    else:
      left.append(rec[-1])
  # print('left: {}'.format(left))
  # print('right: {}'.format(right))
  # print(Counter(left))
  # print(Counter(right))
  return Counter(left), Counter(right)

if __name__ == '__main__':
  records = []
  print('\npart 1')
  with open('4_data_a.txt') as f:
    for line in f:
      records.append(line.split())

  for i in range(5):
    print(class_entropy(i, records))

  print('\npart 2')
  records = []
  with open('4_data_b.txt') as f:
    for line in f:
      records.append(line.split())

  for i in range(5):
    print(class_entropy(i, records))
