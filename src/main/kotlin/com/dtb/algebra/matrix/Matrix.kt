package com.dtb.algebra.matrix

import com.dtb.algebra.utils.AnyUtils.println
import java.util.stream.IntStream


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

	fun cols(): Sequence<Array<Double>> = sequence {
		for (i in 0..<width()) {
			yield(Array(height()) { this@Matrix[i, it] })
		}
	}
	fun rows(): Sequence<Array<Double>> = sequence {
		for (i in 0..<height()) {
			yield(Array(width()) { this@Matrix[it, i] })
		}
	}

	operator fun plus(other: Matrix): Matrix
	operator fun minus(other: Matrix): Matrix

	operator fun times(other: Int): Matrix
	operator fun div(other: Int): Matrix

	operator fun times(other: Double): Matrix
	operator fun div(other: Double): Matrix

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

	override fun clone(): Matrix {
		return Matrix.new(this.width(), this.height()) {
			i, j -> this[i, j]
		}
	}
}