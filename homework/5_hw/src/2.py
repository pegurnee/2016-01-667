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

def confidence(rule, trans):
    inv, outv = rule.split('->')
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
        for f in combinations(sorted(set(''.join(_freq[k]))), k + 2):
            f = ''.join(sorted(set(f)))
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

    print('='*40)
    for s in fset:
        for x in s:
            print('{}: {}'.format(x, fval[x]))

    print(confidence('a->c',trans))
    print(confidence('c->a',trans))
    print(confidence('a->d',trans))
    print(confidence('d->a',trans))
    print(confidence('b->c',trans))
    print(confidence('c->b',trans))
    print(confidence('c->d',trans))
    print(confidence('d->c',trans))

    print('='*40)
    fset, fval = freqset(trans, .25, elems)

    for s in fset:
        for x in s:
            print('{}: {}'.format(x, fval[x]))

    print(support_val('ab', trans))
    print(support_val('ae', trans))
    print(support_val('af', trans))
    print(support_val('be', trans))
    print(support_val('bf', trans))
    print(support_val('df', trans))
    print(support_val('ef', trans))

    print('='*40)

    print(confidence('a->c',trans))
    print(confidence('c->a',trans))
    print(confidence('a->d',trans))
    print(confidence('d->a',trans))

    print(confidence('b->c',trans))
    print(confidence('c->b',trans))
    print(confidence('b->d',trans))
    print(confidence('d->b',trans))

    print(confidence('c->d',trans))
    print(confidence('d->c',trans))
    print(confidence('c->e',trans))
    print(confidence('e->c',trans))
    print(confidence('c->f',trans))
    print(confidence('f->c',trans))

    print(confidence('d->e',trans))
    print(confidence('e->d',trans))

    print('='*40)

    print(confidence('ac->d',trans))
    print(confidence('ad->c',trans))
    print(confidence('cd->a',trans))
    print(confidence('a->cd',trans))
    print(confidence('c->ad',trans))
    print(confidence('d->ac',trans))

    print('='*40)

    print(confidence('bc->d',trans))
    print(confidence('bd->c',trans))
    print(confidence('cd->b',trans))
    print(confidence('b->cd',trans))
    print(confidence('c->bd',trans))
    print(confidence('d->bc',trans))

    print('='*40)
    
    print(confidence('ec->d',trans))
    print(confidence('ed->c',trans))
    print(confidence('cd->e',trans))
    print(confidence('e->cd',trans))
    print(confidence('c->ed',trans))
    print(confidence('d->ec',trans))
