#!/usr/bin/env python3

dpoints = [
    (3,2),
    (5,2),
    (3,1),
    (5,1),
    (9,1)
  ]

def distance(list1, list2):
  _dist = 0
  for a, b in zip(list1, list2):
    _dist += (a - b) ** 2
  return _dist ** (1 / 2)

def centroid(points):
  _sums = []
  for i,_ in enumerate(points[0]):
    _sum = 0
    for p in points:
      _sum += p[i]
    _sums.append(_sum)

  for i,s in enumerate(_sums):
    _sums[i] = s / len(points)

  return tuple(_sums)

if __name__ == '__main__':
  cent = centroid(dpoints[:2])
  print(cent)

  c1 = dpoints[0]
  c2 = dpoints[3]

  for i,p in enumerate(dpoints):
    print("R{} | {} | {}".format(i + 1, distance(p, c1), distance(p, c2)))
