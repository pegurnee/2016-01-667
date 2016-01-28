class DecisionNode:

  def __init__(self, _class, _children=None):
    self._class = _class
    self._children = _children

  def __str__(self):
    if self._children:
      _ret = 'internal'
    else:
      _ret = '%s' % self._class
    return _ret
