import csv

def main():
  with open('../in/SP500.csv') as f:
    _recs = [tuple(_l) for _l in csv.reader(f) if _l[0][3] in '45' and len(_l[1]) > 1]

  percent_vals = []

  for i in range(1, len(_recs)):
    prev_day = float(_recs[i - 1][1])
    curr_day = float(_recs[i][1])
    change = prev_day / curr_day
    change -= 1

    percent_change = change * 100

    if percent_change < -2:
      percent_change = -2
    elif percent_change > 2:
      percent_change = 2

    percent_vals.append((_recs[i][0], percent_change))


  for _r in zip(_recs, percent_vals):
    print('{} : {}'.format(_r[0][0], _r[1]))

  #with open('../in/sp500/train') as train, open('../in/sp500/validate') as valid, open('../in/sp500/test') as testf:



if __name__ == '__main__':
  main()
