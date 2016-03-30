#!/usr/bin/env python3

dpoints = [
    (1,3),
    (4,3),
    (5,3),
    (1,1),
    (4,2)
  ]

def distance(list1, list2):
  _dist = 0
  for a, b in zip(list1, list2):
    _dist += (a - b) ** 2
  return _dist ** (1 / 2)

def print_distance_graph(points):
  print((' ' * 4) + '  '.join(['R{}:'.format(x + 1) for x in range(len(points))]))
  for i,p in enumerate(points):
    print('R{}: '.format(i + 1) + ' '.join([format(distance(p, x), '.2f') for x in points]))

if __name__ == '__main__':
  print_distance_graph(dpoints)
