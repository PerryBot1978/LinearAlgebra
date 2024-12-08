package com.dtb.algebra.matrix

class MatrixView(
	private val width: Int,
	private val height: Int,
	private val xOffset: Int,
	private val yOffset: Int,
	private val parent: Matrix
): AbstractMatrix() {
	override fun width(): Int = this.width
	override fun height(): Int = this.height

	override fun plus(other: Matrix): Matrix {
		if (this.width != other.width() || this.height != other.height())
			throw IllegalArgumentException()
		return Matrix.new(this.width, this.height) { i, j -> this[i, j] + other[i, j] }
	}
	override fun minus(other: Matrix): Matrix {
		if (this.width != other.width() || this.height != other.height())
			throw IllegalArgumentException()
		return Matrix.new(this.width, this.height) { i, j -> this[i, j] - other[i, j] }
	}

	override fun times(other: Int): Matrix {
		return Matrix.new(this.width, this.height) { i, j -> this[i, j] * other }
	}
	override fun times(other: Double): Matrix {
		return Matrix.new(this.width, this.height) { i, j -> this[i, j] * other }
	}

	override fun times(other: Matrix): Matrix {
		return (this.clone() as NativeMatrix) * other
	}

	override fun div(other: Int): Matrix {
		return Matrix.new(this.width, this.height) { i, j -> this[i, j] / other }
	}
	override fun div(other: Double): Matrix {
		return Matrix.new(this.width, this.height) { i, j -> this[i, j] / other }
	}

	override fun get(i: Int, j: Int): Double {
		if (i > this.width || j > this.height)
			throw IndexOutOfBoundsException()
		return parent[i + this.xOffset, j + this.yOffset]
	}
}