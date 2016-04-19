#!/usr/bin/env python3

dataset = [
    (10,22),
    (15,29),
    (12,27),
    (13,24),
    (20,33)
]

def mean_adjusted(matrix):
    _means = []

    for c in matrix[0]:
        _means.append(0)

    for r in matrix:
        for i,c in enumerate(r):
             _means[i] += c

    print(_means)
    _means = [ x/len(matrix) for x in _means]

    print(_means)
    new_data = []

    for row in matrix:
        new_data.append([ v - m for v,m in zip(row, _means) ])

    return new_data

if __name__ == '__main__':
    print(mean_adjusted(dataset))
