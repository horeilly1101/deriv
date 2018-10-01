'''Polynomial class
'''
from term import Term
from factor import

class Poly(Factor):
	'''
	'''
	def __init__(power = 1, var='x'):
		self.power = power
		self.var = var

	def multiply(poly):
		return Poly(self.power + poly.power)
