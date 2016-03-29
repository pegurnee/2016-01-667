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

  clus1 = []
  clus2 = []
  while True:
    was_swap = False
    for p in dpoints:
      if (distance(p, c1) < distance(p, c2)):
        if p not in clus1:
          clus1.append(p)
          was_swap = True
        if p in clus2:
          clus2.remove(p)
      else:
        if p not in clus2:
          clus2.append(p)
          was_swap = True
        if p in clus1:
          clus1.remove(p)
    c1 = centroid(clus1)
    c2 = centroid(clus2)

    print("State:")
    print("Clusters:")
    print("Cluster1:\n\tCentroid:{}\n\tPoints: {}".format(c1, clus1))
    print("Cluster2:\n\tCentroid:{}\n\tPoints: {}".format(c2, clus2))

    if not was_swap:
      break
