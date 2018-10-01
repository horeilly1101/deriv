'''Constant class'''
from functools import reduce

class Constant():
  def __init__(self, *args):
      self.constants = args if args else 1

  def compute_float(self):
    return reduce(lambda prod, const: prod * const, float(self.constants), 1)
