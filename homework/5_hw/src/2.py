#!/usr/bin/env python3

from itertools import combinations

elems = list('abcdef')

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

def support_val(vals, trans):
    return support(vals, trans) / len(trans)

def freqset(trans, support_delta, elems):
    _freq = []
    _infreq = []
    _svals = {}


    _els = []
    for x in elems:
        sval = support_val([x], trans)
        if not sval < support_delta:
            _els.append(x)
            _svals[x] = sval

    _freq.append(_els)

    k = 0
    while True:
        potential_new = []
        for f in combinations(_freq[k], k + 2):
            if f in _infreq:
                continue
            sval = support_val(f, trans)
            if not sval < support_delta:
                potential_new.append(f)
            else:
                _infreq.append(f)

        if not potential_new:
            break

        _freq.append(potential_new)
        k += 1

    return _freq



if __name__ == '__main__':
    print(support('bc', trans))

    print(freqset(trans, .5, elems))
