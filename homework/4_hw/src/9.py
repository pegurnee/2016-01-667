#!/usr/bin/env python3

dpoints = [
  (1,3),
  (2,3),
  (3,3),
  (1,2),
  (2,2),
  (3,2),
  (5,2),
  (5,1),
  (6,1)
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

def is_core(point, distance_threshold, mincount, points):
  _num = 0
  for p in points:
    if (distance(p, point) <= distance_threshold):
      _num += 1
  return True if _num >= mincount else False

def is_boundary(point, distance_threshold, cores):
  for c in cores:
    if (distance(c, point) < distance_threshold):
      return True
  return False

if __name__ == '__main__':
  dist = 1
  count = 3

  ugly_cheating = []
  cores = []

  for p in dpoints:
    if is_core(p, dist, count, dpoints):
      cores.append(p)
      ugly_cheating.append('core')
    else:
      ugly_cheating.append('')

  for i,p in enumerate(dpoints):
    if ugly_cheating[i] != 'core':
      if is_boundary(p, dist, cores):
        ugly_cheating[i] = 'boundary'
      else:
        ugly_cheating[i] = 'noise'

  print(ugly_cheating)
