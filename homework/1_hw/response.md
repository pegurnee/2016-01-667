1.
  a.
    i. Classification - determining if an e-mail is spam
    ii. Prediction - forecast the price of stocks
    iii. Pattern analysis - finding items customers purchase together
    iv. Clustering - grouping together similar news articles
  b. NOT COMPLETE
    i. Feature selection - excluding irrelevant data such as identifiers
    ii. Feature construction - create new attributes from original features
    iii. Transformation - formatting raw data to a more usable form
  c.
    supervised learning has an additional step of training the system of what and how to recognize data before the actual algorithm is used with live data that does not exist with unsupervised learning
2.
  a. NOT COMPLETE
    i. nominal - marital status, color,
    ii. ordinal - age, grade,
    iii. binary - in state, sex, married
    iv. continuous - height, gpa, temperature
  b.
    applying the floor function to temperature so that there are only integer values
  c.
    taking marital status (single, married, divorced, widow) and only treating single, divorced, and widow as not-married
  d.
    treating gpa as a value from 0-1 by dividing by 4 (the max)
3.
  a.
    20+10+5+9+14+25+15+9 = 107
    mean => 107 / 8 = 13.375
  b.
    ((20 - 13.375) ^ 2) + ((10 - 13.375) ^ 2) + ((5 - 13.375) ^ 2) + ((9 - 13.375) ^ 2) + ((14 - 13.375) ^ 2) + ((25 - 13.375) ^ 2) + ((15 - 13.375) ^ 2) + ((9 - 13.375) ^ 2) = 301.875
    variance => 301.875 / 8 = 37.734375
    stdev => sqrt(37.734375) = 6.142831187652807
  c.
    sort -> [5, 9, 9, 10, 14, 15, 20, 25]
    middle value(s) -> (10, 14)
    median (mean of both values) -> 12
4.
  a.
    sort -> [60, 70, 75, 80, 85, 90, 90]
    max index -> length - 1 = 7 - 1 = 6
    i.
      index = ceil(6 * 30 / 100) = ceil(1.8) = 2
      30th percentile = arr[2] = 75
    ii.
      index = ceil(6 * 80 / 100) = ceil(4.8) = 5
      80th percentile = arr[5] = 90
  b.
    five point summary = (min, 25th percentile, median, 75th percentile, max)
    (60, 70, 80, 90, 90)
5.
  a.
    0.1954615382218836
  b.
    the correlation is minorly positive, there is a slight trend that as the weight values go up, higher ages are observed
6.
  a.
    3.4641016151377544
  b.
    16
  c.
    s1 & s2 = 3.4641016151377544
    s2 & s3 = 8.12403840463596
    s3 & s1 = 7.874007874011811
    as the euclidean distance between s1 & s2 is the smallest among the students, s1 and s2 are the most similar
7.
  a.
    8/10
  b.
    3/5
  c.
    i. 996/1000
    ii. 0/4
    iii. in this situation, Jacard's coefficient gave the most correct result as the people purchased none of the same items, encouraging the thought that they are not alike. matching coefficient gives a false positive of similarity
8.
  a.
  b.
  c.
  d.
9.
  a.
  b.
10.
  a.
  b.
