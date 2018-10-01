'''Term Class
'''

class Term:
	'''a set of factors and a Constant object
	'''
	def __init__(self, terms, constant = Constant(), variable='x'):
		self.variable = variable
		self.constants = [const]
		self.factors = set(args) # function objects

	def add_factor(self, factor):
		return Term()

	def __str__(self):
		return str(self.const) + str(self.factors)

a = Term(4, 'x', 3, 7)
a.add_factor(7)
print(a)
