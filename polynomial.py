'''Polynomial class
'''
from term import Term
from factor import

class Poly(Factor):
	'''a polynomial (extends Factor class)

	instance variables:

	'''
	def __init__(self, power = 1, var='x'):
		self.power = power
		# self.inside =

	def multiply(poly):
		return Poly(self.power + poly.power)
