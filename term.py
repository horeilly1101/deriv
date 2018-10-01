'''Term Class
'''

class Term():
	'''a set of factors and a constant
	'''
	def __init__(self, const = '1', variable='x', *args):
		self.variable = variable
		self.constants = [const]
		self.factors = set(args)

	def add_factor(self, factor):
		return Term()

	def __str__(self):
		return str(self.const) + str(self.factors)

	def derive(self, variable='x'):
		if type

a = Term(4, 'x', 3, 7)
a.add_factor(7)
print(a)
