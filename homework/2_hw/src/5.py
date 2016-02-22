records = [
  (3,   2 ,  10,   2),
  (2,   1 ,  2 ,   1),
  (5,   18,  9 ,   2),
  (2,   5 ,  4 ,   1),
  (4,   3 ,  2 ,   1),
  (4,   17,  12,   2)
]

def distances(rec1, records, dis):
  _ds = []
  for r in records:
    _ds.append(dis(rec1, r))
  return _ds

def euclid(rec1, rec2):
  _dist = 0
  for u,v in zip(rec1, rec2):
    _dist += (u - v) ** 2
  return _dist ** 0.5

def taxi(rec1, rec2):
  _dist = 0
  for u,v in zip(rec1, rec2):
    _diff = u - v
    _dist += -_diff if _diff < 0 else _diff
  return _dist

if __name__ == '__main__':
  a = (6,7,10)
  ads = distances(a, records, euclid)
  print(ads)
  print('2: {}'.format(1 / ads[0]))
  _1 = 1 / ads[3]
  _2 = 1 / ads[4]
  print('1: {} = {} + {}'.format(_1 + _2, _1, _2))


  b = (4, 9, 5)
  print(distances(b, records, taxi))
