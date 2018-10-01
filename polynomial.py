'''Polynomial class
'''
from term import Term
from factor import Factor
from constant import Constant

class Poly(Factor):
	'''a polynomial (extends Factor class)

	instance variables:
		var (super) -- a variable

		power -- a float that corresponds to the
			power of the inside
	'''
	def __init__(self, power = 1, var='x'):
		self.power = power
		super.__init__(self, var)

	def multiply(poly):
		return Poly(self.power + poly.power)

	def differentiate(self):
		return (Constant(self.power - 1), Poly(self.power - 1)) \
			if not self.power in (0, 1) else Constant()
