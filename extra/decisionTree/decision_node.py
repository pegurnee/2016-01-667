class DecisionNode:

  def __init__(self, _class, _attribute=None):
    self._class = _class
    self._attribute = _attribute

    self._children = {}

  def __str__(self):
    if self._children:
      _ret = 'internal %s' % self._attribute
    else:
      _ret = '%s' % self._class
    return _ret

  def _print(self, spaces=0, opt=''):
    print(' ' * spaces + str(self) + '     \t' + opt)
    for key,child in self._children.items():
      child._print(spaces + 2, key)
