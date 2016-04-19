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

def confidence(inv, outv, trans):
    sin = support(inv + outv, trans)
    sout = support(inv, trans)
    return sin / sout

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
                _svals[f] = sval
            else:
                _infreq.append(f)

        if not potential_new:
            break

        _freq.append(potential_new)
        k += 1

    return _freq, _svals



if __name__ == '__main__':
    print(support('bc', trans))
    print(support('acd', trans))

    fset, fval = freqset(trans, .5, elems)

    for s in fset:
        for x in s:
            print('{}: {}'.format(x, fval[x]))

    print(confidence('a','c',trans))
    print(confidence('c','a',trans))
    print(confidence('a','d',trans))
    print(confidence('d','a',trans))
    print(confidence('b','c',trans))
    print(confidence('c','b',trans))
    print(confidence('c','d',trans))
    print(confidence('d','c',trans))
