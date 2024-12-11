package com.dtb.algebra.matrix

import com.dtb.algebra.matrix.transforms.HorizontalMatrixMerge
import com.dtb.algebra.matrix.transforms.MatrixMinor
import com.dtb.algebra.matrix.transforms.MatrixView
import com.dtb.algebra.matrix.transforms.VerticalMatrixMerge

import java.util.stream.IntStream
import kotlin.math.pow
import kotlin.streams.asSequence

interface Matrix: Cloneable {
	companion object {
		operator fun Int.times(other: Matrix): Matrix = other * this
		operator fun Double.times(other: Matrix): Matrix = other * this

		fun new(width: Int, height: Int): Matrix = ConcreteMatrix(width, height)
		fun new(width: Int, height: Int, initializer: (Int, Int) -> Double): Matrix = ConcreteMatrix(width, height, initializer)

		fun lazy(width: Int, height: Int, calc: (Int, Int) -> Double): Matrix = LazyMatrix(width, height, calc)

		/**
		 * Creates a matrix that has lazy initialized values, but will cache values that are initialized
		 *
		 * @param width The width of the returned matrix
		 * @param height The height of the returned matrix
		 * @param calc The function to initialize an index
		 * @return CachingMatrix
		 */
		fun caching(width: Int, height: Int, calc: (Int, Int) -> Double): Matrix = CachingMatrix(width, height, calc)

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
		return Matrix.lazy(this.width(), this.height()) { i, j -> this[i, j] + other[i, j] }
	}
	operator fun minus(other: Matrix): Matrix {
		if (this.width() != other.width() || this.height() != other.height())
			throw IllegalArgumentException()
		return Matrix.lazy(this.width(), this.height()) { i, j -> this[i, j] - other[i, j] }
	}

	operator fun times(other: Int): Matrix = Matrix.lazy(this.width(), this.height()) { i, j -> this[i, j] * other }
	operator fun div(other: Int): Matrix = Matrix.lazy(this.width(), this.height()) { i, j -> this[i, j] / other }

	operator fun times(other: Double): Matrix = Matrix.lazy(this.width(), this.height()) { i, j -> this[i, j] * other }
	operator fun div(other: Double): Matrix = Matrix.lazy(this.width(), this.height()) { i, j -> this[i, j] / other }

	operator fun times(other: Matrix): Matrix {
		if (this.width() != other.height())
			throw IllegalArgumentException("Invalid matrix argument size for Matrix.times(Matrix)")

		return Matrix.lazy(this.height(), other.width()) { i, j ->
			IntStream
				.range(0, other.height())
				.mapToDouble { k -> this[k, j] * other[i, k] }
				.reduce(Double::plus)
				.orElse(0.0)
		}
	}
	operator fun get(i: Int, j: Int): Double

	fun combineHorizontal(other: Matrix): Matrix = HorizontalMatrixMerge(this, other)
	fun combineVertical(other: Matrix): Matrix = VerticalMatrixMerge(this, other)
	fun combine4(topRight: Matrix, bottomLeft: Matrix, bottomRight: Matrix): Matrix =
		VerticalMatrixMerge(
			HorizontalMatrixMerge(this, topRight),
			HorizontalMatrixMerge(bottomLeft, bottomRight)
		)

	fun split4(): Array<Matrix> {
		return Array(4) { num ->
			MatrixView(
				this.width() / 2,
				this.height() / 2,
				if (num % 2 == 1) this.width() / 2 else 0,
				if (num > 1) this.height() / 2 else 0,
				this
			)
		}
	}
	fun transpose(): Matrix = Matrix.lazy(this.height(), this.width()) { i, j -> this[j, i] }
	fun minor(i: Int, j: Int): Matrix = MatrixMinor(i, j, this)

	fun determinate(): Double {
		if (this.width() != this.height())
			throw IllegalArgumentException("Height must equal width")

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

		val matrix = this.concrete()
		// Laplace Reduction
		return IntStream
			.range(0, matrix.width())
			.parallel()
			.mapToDouble {
				val sign = (-1.0).pow(it + 0)
				val multiple = matrix[it, 0]
				val determinate = matrix
					.minor(it, 0)
					.determinate()
				sign * multiple * determinate
			}.reduce(Double::plus)
			.orElse(1.0)
	}

	fun cofactor(): Matrix = Matrix.lazy(this.width(), this.height()) { i, j ->
		(-1.0).pow(i + j) * this.minor(i, j).determinate()
	}
	fun adjunct(): Matrix = this.cofactor().transpose()

	/**
	 * Finds the inverse of a matrix, if possible
	 *
	 * @throws IllegalArgumentException If matrix has a determinate of 0
	 * @throws IllegalArgumentException If matrix is not square
	 */
	fun inverse(): Matrix {
		val det = this.determinate()
		if (det == 0.0)
			throw IllegalArgumentException("Matrix had determinate of 0")
		return this.adjunct() / det
	}

	/**
	 * Converts a matrix to a NativeMatrix
	 *
	 * Equivalent to Matrix.clone()
	 */
	fun concrete() = this.clone()

	/**
	 * Converts a matrix to a NativeMatrix
	 *
	 * Equivalent to Matrix.concrete()
	 */
	override fun clone(): Matrix =
		Matrix.new(this.width(), this.height()) {
			i, j -> this[i, j]
		}
}