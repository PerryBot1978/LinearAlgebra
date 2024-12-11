package com.dtb.algebra.matrix

/**
 * Represents a vector as an implementation of Matrix
 *
 * @param values The values to form the matrix. The list is copied on construction.
 */
class Vector(values: List<Double>): AbstractMatrix() {
	private val values = values
		.stream()
		.toList()

	/**
	 * Creates a vector with the same values of the passed matrix.
	 *
	 * @param matrix Matrix to copy values from
	 * @throws IllegalArgumentException If matrix.width() != 1
	 */
	constructor(matrix: Matrix) : this(matrix.cols().first()) {
		if (matrix.width() != 1)
			throw IllegalArgumentException("Matrix $matrix is not convertable to a vector")
	}

	operator fun get(index: Int): Double = this.values[index]
	override fun get(i: Int, j: Int): Double = this[j]

	override fun width(): Int = 1
	override fun height(): Int = this.values.size
}