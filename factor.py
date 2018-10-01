'''Factor class'''

from term import Term
from constant import Constant

class Factor(Function):
	'''a function without constants

	instance variables:
		var (string) -- a variable
	'''
	def __init__(terms, var = 'x'):
		self.constant = constant
		self.var = var
		self.terms = set(terms)
