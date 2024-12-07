package com.dtb.algebra.matrix

interface Matrix: Cloneable {
	companion object {
		operator fun Int.times(other: Matrix): Matrix = other * this
		operator fun Double.times(other: Matrix): Matrix = other * this

		fun new(width: Int, height: Int): Matrix = NativeMatrix(width, height)
		fun new(width: Int, height: Int, initializer: (Int, Int) -> Double): Matrix = NativeMatrix(width, height, initializer)
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

	operator fun times(other: Matrix): Matrix
	operator fun get(i: Int, j: Int): Double

	fun combineHorizontal(other: Matrix): Matrix = NativeMatrix(this.width() + other.width(), this.height()) { i, j ->
		if (i < this.width()) this[i, j] else other[i - this.width(), j]
	}
	fun combineVertical(other: Matrix): Matrix = NativeMatrix(this.width(), this.height() + other.height()) { i, j ->
		if (j < this.height()) this[i, j] else other[i, j - this.height()]
	}

	override fun clone(): Matrix {
		return NativeMatrix(this.width(), this.height()) {
				i, j -> this[i, j]
		}
	}
}