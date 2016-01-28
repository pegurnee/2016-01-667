from decision_node import DecisionNode

def build(_recs, _atts=None):

  if _same_class(_recs):
    _ret_node = DecisionNode(_recs[0][-1])
  elif not _atts:
    _ret_node = DecisionNode(_majority_class(_recs))
  else:
    _cond = _best(_recs, _atts)
    _sel_att, _outcomes = _cond

    _partitions = []
    for _o in _outcomes:
      _satisfied = _satisfy(_recs, _sel_att, _o)
      if _satisfied:
        _partitions.append(_satisfied)

    if len(_partitions) == 1:
      _ret_node = DecisionNode(_majority_class(_recs))
    else:
      

  return _ret_node



def _best(_recs, _atts):
  pass

def _satisfy(_recs, _attribute, _outcome):
  _ret = []
  for _r in _recs:
    if _r[_attribute] == _outcome:
      _ret.append(_r)
  return _ret

def _same_class(_recs):
  _class = _recs[0][-1]
  for _r in _recs:
    if _class != _r[-1]:
      return False
  return True

def _majority_class(_recs):
  _classes = []
  for _r in _recs:
    _classes.append(_r[-1])
  return _mode(_classes)

def _mode(arr):
  return max(set(arr), key=arr.count)
