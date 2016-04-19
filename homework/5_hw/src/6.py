#!/usr/bin/env python3
centroids = [
    (9.2,9.8),
    (4.0, 3 + (2/3))
]

recs = [
    [
        (12,9),
        (11,10),
        (7,8),
        (10,12),
        (6,10)
    ],
    [
        (5,3),
        (4,4),
        (3,4)
    ]
]

def distance(rec1, rec2):
    dist = 0.0
    for x,y in zip(rec1, rec2):
        dist += ((x - y) ** 2)

    return dist ** (1 / 2)

if __name__ == '__main__':
    for cen,g in zip(centroids, recs):
        for r in g:
            print('{}: {}'.format(r, distance(cen, r)))
