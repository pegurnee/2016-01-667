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

  percent_recs = []
  for i in range(3, len(percent_vals)):
    date = percent_vals[i][0]

    fourple = percent_vals[i - 3 : i + 1]
    record = tuple([ i[1] for i in fourple ])
    percent_recs.append((date, record))

  for _r in percent_recs:
    print('{} : {}'.format(_r[0], _r[1]))

  # for _r in zip(_recs, percent_vals):
  #   print('{} : {}'.format(_r[0][0], _r[1]))
  training_recs = [x for x in percent_recs if x[0][3] == '4']
  validation_recs = [x for x in percent_recs if x[0][3] == '5' and int(x[0][5:7]) < 4]
  testing_recs = [x for x in percent_recs if x[0][3] == '5' and int(x[0][5:7]) == 4]

  print('='*40)
  for _r in training_recs:
    print('{} : {}'.format(_r[0], _r[1]))

  with open('../in/sp500/train', 'w') as train, open('../in/sp500/validate', 'w') as valid, open('../in/sp500/test', 'w') as testf:
    train.write('{} {} {}\n'.format(len(training_recs), len(training_recs[0][1]) - 1, 1))
    for _r in training_recs:
      train.write(' '.join(map(str, _r[1])))
      train.write('\n')

    valid.write('{}\n'.format(len(validation_recs)))
    for _r in validation_recs:
      valid.write(' '.join(map(str, _r[1])))
      valid.write('\n')

    testf.write('{}\n'.format(len(testing_recs)))
    for _r in testing_recs:
      testf.write(' '.join(map(str, _r[1])))
      testf.write('\n')




if __name__ == '__main__':
  main()
