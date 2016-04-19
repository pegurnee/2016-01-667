#!/usr/bin/env python3

points = [
    (3,5),
    (4,4),
    (10,9),
    (5,6),
    (3,10),
    (4,3)
]

def distance(rec1, rec2):
    dist = 0.0
    for x,y in zip(rec1, rec2):
        dist += ((x - y) ** 2)

    return dist ** (1 / 2)

if __name__ == '__main__':
    for x in points:
        for y in points:
            print('{} x {}: {}'.format(x,y,distance(x,y)))

    print(distance(points[0], points[1]) + distance(points[0], points[3]))
    print(distance(points[1], points[5]) + distance(points[1], points[0]))
    print(distance(points[2], points[3]) + distance(points[2], points[4]))

    print(distance(points[3], points[0]) + distance(points[3], points[1]))
    print(distance(points[4], points[0]) + distance(points[4], points[3]))
    print(distance(points[5], points[1]) + distance(points[5], points[0]))
