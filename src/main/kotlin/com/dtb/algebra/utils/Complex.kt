package com.dtb.algebra.utils

import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.util.stream.IntStream

/**
 * Represents a complex number of value a + bi
 *
 * Call Complex.get(a, b) to get an instance of Complex
 */
class Complex private constructor(private var a: Double, private var b: Double){
	companion object {
		private val referenceQueue = ReferenceQueue<Complex>()
		fun get(a: Double, b: Double): Complex {
			if (referenceQueue.poll() != null) {
				val out = referenceQueue.remove().get()!!
				out.a = a
				out.b = b
				return out
			}

			val out = Complex(a, b)
			PhantomReference(out, referenceQueue)
			return out
		}
		fun get(a: Int, b: Int): Complex = get(a.toDouble(), b.toDouble())
		fun get(a: Double, b: Int): Complex = get(a, b.toDouble())
		fun get(a: Int, b: Double): Complex = get(a.toDouble(), b)
	}

	operator fun unaryPlus() = this
	operator fun unaryMinus() = Complex(-this.a, -this.b)

	operator fun plus(other: Complex) = Complex(this.a + other.a, this.b + other.b)
	operator fun minus(other: Complex) = Complex(this.a - other.a, this.b - other.b)

	operator fun times(other: Double) = Complex(this.a * other, this.b * other)
	operator fun times(other: Complex) = Complex(this.a * other.a - this.b * other.b, this.a * other.b + other.a * this.b)

	operator fun div(other: Double) = Complex(this.a / other, this.b / other)
	operator fun div(other: Complex) = Complex(this.a * other.a + this.b * other.b, this.b * other.a - this.a * other.b) / (other.a * other.a + other.b * other.b)

	fun conjugate() = Complex(this.a, -this.b)
	fun pow(power: Int): Complex = IntStream
		.range(0, power)
		.mapToObj { this }
		.reduce(Complex::times)
		.orElse(Complex(1.0, 0.0))
}