records = [
  (1,0,0,1,0,2),
  (0,1,0,1,0,1),
  (1,1,1,0,0,2),
  (0,1,1,1,0,2),
  (1,0,0,1,0,1)
]

def distances(rec1, records, dis):
  _ds = []
  for r in records:
    _ds.append(dis(rec1, r))
  return _ds

def match_co(rec1, rec2):
  _m = 0
  for u,v in zip(rec1[:-1], rec2[:-1]):
    _m += 1 if u == v else 0
  return 1 - (_m / len(rec1[:-1]))

if __name__ == '__main__':
  a = (1,0,1,0,1)

  distys = distances(a, records, match_co)
  print(distys)

  for d in distys:
    print(1 / d)
