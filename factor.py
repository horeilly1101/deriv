'''Factor class'''

from term import Term
from constant import Constant

class Factor():
	'''a factor

	instance variables:
		var (string) -- a variable
		constant -- a Constant object
	'''
	def __init__(constant = Constant(), var='x'):
		self.var = var
		self.constant = constant
