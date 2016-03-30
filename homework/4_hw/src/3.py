#!/usr/bin/env python3

dpoints = [
  (0,4),
  (2,4),
  (0,0),
  (2,0),
  (8,2)
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

def print_distance_graph(points):
  print((' ' * 4) + '  '.join(['R{}:'.format(x + 1) for x in range(len(points))]))
  for i,p in enumerate(points):
    print('R{}: '.format(i + 1) + ' '.join([format(distance(p, x), '.2f') for x in points]))

if __name__ == '__main__':
  print_distance_graph(dpoints)
