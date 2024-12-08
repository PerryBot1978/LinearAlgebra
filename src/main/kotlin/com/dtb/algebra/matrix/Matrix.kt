package com.dtb.algebra.matrix

import java.util.stream.IntStream
import kotlin.math.pow
import kotlin.streams.asSequence

interface Matrix: Cloneable {
	companion object {
		operator fun Int.times(other: Matrix): Matrix = other * this
		operator fun Double.times(other: Matrix): Matrix = other * this

		fun new(width: Int, height: Int): Matrix = NativeMatrix(width, height)
		fun new(width: Int, height: Int, initializer: (Int, Int) -> Double): Matrix = NativeMatrix(width, height, initializer)

		fun of(str: String): Matrix {
			val values = str
				.split(";")
				.map { it.split(",").map(String::toDouble) }
			return Matrix.new(values.size, values[0].size) { i, j -> values[j][i] }
		}
	}

	fun width(): Int
	fun height(): Int

	fun cols(): Sequence<Array<Double>> = IntStream
		.range(0, this@Matrix.width())
		.mapToObj { Array(this@Matrix.height()) { i -> this@Matrix[it, i] } }
		.asSequence()
	fun rows(): Sequence<Array<Double>> = IntStream
		.range(0, this@Matrix.height())
		.mapToObj { Array(this@Matrix.width()) { i -> this@Matrix[i, it] } }
		.asSequence()

	operator fun plus(other: Matrix): Matrix {
		if (this.width() != other.width() || this.height() != other.height())
			throw IllegalArgumentException()
		return Matrix.new(this.width(), this.height()) { i, j -> this[i, j] + other[i, j] }
	}
	operator fun minus(other: Matrix): Matrix {
		if (this.width() != other.width() || this.height() != other.height())
			throw IllegalArgumentException()
		return Matrix.new(this.width(), this.height()) { i, j -> this[i, j] - other[i, j] }
	}

	operator fun times(other: Int): Matrix = Matrix.new(this.width(), this.height()) { i, j -> this[i, j] * other }
	operator fun div(other: Int): Matrix = Matrix.new(this.width(), this.height()) { i, j -> this[i, j] / other }

	operator fun times(other: Double): Matrix = Matrix.new(this.width(), this.height()) { i, j -> this[i, j] * other }
	operator fun div(other: Double): Matrix = Matrix.new(this.width(), this.height()) { i, j -> this[i, j] / other }

	operator fun times(other: Matrix): Matrix {
		if (this.width() != other.height())
			throw IllegalArgumentException("Invalid matrix argument size for Matrix.times(Matrix)")

		return Matrix.new(this.height(), other.width()) { i, j ->
			IntStream
				.range(0, other.height())
				.mapToDouble { k -> this[k, j] * other[i, k] }
				.reduce(Double::plus)
				.orElse(0.0)
		}
	}
	operator fun get(i: Int, j: Int): Double

	fun combineHorizontal(other: Matrix): Matrix = NativeMatrix(this.width() + other.width(), this.height()) { i, j ->
		if (i < this.width()) this[i, j] else other[i - this.width(), j]
	}
	fun combineVertical(other: Matrix): Matrix = NativeMatrix(this.width(), this.height() + other.height()) { i, j ->
		if (j < this.height()) this[i, j] else other[i, j - this.height()]
	}
	fun combine4(topRight: Matrix, bottomLeft: Matrix, bottomRight: Matrix): Matrix = this.combineHorizontal(topRight).combineVertical(bottomLeft.combineHorizontal(bottomRight))

	fun split4(views: Boolean = true): Array<Matrix> {
		return Array(4) { num ->
			if (views)
				MatrixView(
					this.width() / 2,
					this.height() / 2,
					if (num % 2 == 1) this.width() / 2 else 0,
					if (num > 1) this.height() / 2 else 0,
					this
				)
			else
				Matrix.new(this.width() / 2, this.height() / 2) { i,j ->
					var x = i
					var y = j

					if (num % 2 == 1) x += this.width() / 2
					if (num >= 2) y += this.height() / 2

					this[x, y]
				}
		}
	}
	fun transpose(): Matrix = Matrix.new(this.height(), this.width()) { i,j -> this[j, i] }

	fun minor(i: Int, j: Int): Matrix = MatrixMinor(i, j, this)

	fun determinate(): Double{
		if (this.width() != this.height())
			TODO()

		if (this.width() == 0 && this.height() == 0)
			return 1.0
		if (this.width() == 1 && this.height() == 1)
			return this[0, 0]
		if (this.width() == 2 && this.height() == 2)
			return this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0]

		// Sarrus Rule
		if (this.width() == 3 && this.height() == 3) {
			val prod1 = this[0, 0] * this[1, 1] * this[2, 2]
			val prod2 = this[1, 0] * this[2, 1] * this[0, 2]
			val prod3 = this[2, 0] * this[0, 1] * this[1, 2]
			val prod4 = this[0, 2] * this[1, 1] * this[2, 0]
			val prod5 = this[0, 1] * this[1, 0] * this[2, 2]
			val prod6 = this[0, 0] * this[2, 1] * this[1, 2]
			return prod1 + prod2 + prod3 - prod4 - prod5 - prod6
		}

		// Laplace Reduction
		return IntStream
			.range(0, this.width())
			.parallel()
			.mapToDouble { i ->
				val sign = (-1.0).pow(i + 0)
				val multiple = this[i, 0]
				val determinate = this.minor(i, 0).determinate()
				sign * multiple * determinate
			}.sum()
	}

	fun cofactor(): Matrix = Matrix.new(this.width(), this.height()) { i, j ->
		(-1.0).pow(i + j) * this.minor(i, j).determinate()
	}
	fun adjunct(): Matrix = this.cofactor().transpose()
	fun inverse(): Matrix {
		val det = this.determinate()
		if (det == 0.0)
			throw IllegalArgumentException("Matrix had determinate of 0")
		return this.adjunct() / det
	}

	override fun clone(): Matrix =
		Matrix.new(this.width(), this.height()) {
			i, j -> this[i, j]
		}
}