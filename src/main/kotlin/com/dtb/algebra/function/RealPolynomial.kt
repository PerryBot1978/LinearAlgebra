package com.dtb.algebra.function

class RealPolynomial(val coefficients: List<Double>): RealFunction {
	constructor(vararg x: Double): this(x.toList())

	override fun invoke(x: Double): Double {
		var xPow = 1.0
		return coefficients
			.stream()
			.mapToDouble { c: Double ->
				val out = c * xPow
				xPow *= x
				out
			}.sum()
	}
}