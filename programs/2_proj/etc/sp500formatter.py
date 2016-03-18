import csv

def main():
  with open('../in/SP500.csv') as f:
    _recs = [tuple(_l) for _l in csv.reader(f) if _l[0][3] in '45' and len(_l[1]) > 1]

    for _r in _recs:
      print('%s : %s' % (_r[0], _r[1]))


if __name__ == '__main__':
  main()
