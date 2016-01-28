from build import build

import csv

def main():

  with open('test/data.csv', newline='') as f:
    _recs = [tuple(_l) for _l in csv.reader(f)]

  for _r in _recs:
    print('%s : %s' % (len(_r), _r))

  print(build(_recs))

if __name__ == '__main__':
  main()
