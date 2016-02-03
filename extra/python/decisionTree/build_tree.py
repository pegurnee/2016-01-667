from build import build

import csv

def main():

  with open('test/data.csv', newline='') as f:
    _recs = [tuple(_l) for _l in csv.reader(f)]

  for _r in _recs:
    print('%s : %s' % (len(_r), _r))

  tree = build(_recs, (0, 1, 2, 3, 4))

  tree._print()

if __name__ == '__main__':
  main()
