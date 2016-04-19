#!/usr/bin/env python3

trans = [
    ('b','c','f'),
    ('a','c','d','e'),
    ('a','b','c','d'),
    ('b','c','d','e'),
    ('a','c','d','f')
]

def support(vals, trans):
    _s = 0
    for t in trans:
        if set(vals).issubset(t):
            _s += 1
    return _s


if __name__ == '__main__':
    print(support(('b','c'), trans))
