records = [
  (3.4,         40,            600,               2),
  (2.8,         25,            800,               1),
  (3.5,         30,            700,               3),
  (3.0,         45,            650,               2)
]
ranges = [
  (0, 4),
  (20, 60),
  (500, 1000)
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

def normalize(x, limit):
  return (x - limit[0]) / (limit[1] - limit[0])

def make_normal(rec, ranges):
  _ret = []
  for i,r in enumerate(ranges):
    _ret.append(normalize(rec[i], r))
  return tuple(_ret)

if __name__ == '__main__':
  normal = []

  for rec in records:
    normal.append(make_normal(rec, ranges))

  print(normal)

  raw     = (3.2, 35, 700)
  normed  = make_normal(raw, ranges)
  distys  = distances(normed, normal, euclid)
  print(distys)

  _1 = 1 / distys[0]
  _2 = 1 / distys[3]
  print(_1)
  print(1 / distys[2])
  print(_2)
  print(_1 + _2)
