print(','.join('''college!! smoker! ! ! married!! ! female! ! retired!! highrisk

highschool! ! nonsmoker! ! married!! ! female! ! works! ! mediumrisk

college!! ! nonsmoker! ! notmarried! ! female! ! works! ! lowrisk

college!! ! smoker! ! ! notmarried! ! male! ! retired!! undetermined

highschool! ! smoker! ! ! notmarried! ! female! ! retired!! lowrisk!!

college!! ! nonsmoker! ! married!! ! female! ! works! ! mediumrisk! !

college!! ! smoker! ! ! notmarried! ! male! ! works! ! undetermined

highschool! ! smoker! ! ! notmarried! ! male! ! retired!! undetermined

college!! ! smoker! ! ! notmarried! ! female! ! works! ! lowrisk!! !

college!! ! nonsmoker! ! married!! ! female! ! retired!! mediumrisk! ! !

college!! ! smoker! ! ! married!! ! male! ! works! ! highrisk! ! !

highschool! ! smoker! ! ! notmarried! ! male! ! works! ! undetermined! !

college!! ! nonsmoker! ! notmarried! ! female! ! retired!! lowrisk!!

highschool! ! smoker! ! ! married!! ! male! ! works! ! highrisk'''.replace('!', '').replace('\n','|').split()).replace('||','\n'))
