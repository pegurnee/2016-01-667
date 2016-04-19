#!/usr/bin/env python3

recs = [
    (10,5,20),
    (13,15,5),
    (15,9,23),
    (16,10,10),
    (10,12,30)
]

def distance(rec1, rec2):
    dist = 0.0
    for x,y in zip(rec1,rec2):
        dist += abs(x - y)

    return dist


if __name__ == '__main__':
    target = (12,10,20)

    for r in recs:
        print(distance(target,r))
