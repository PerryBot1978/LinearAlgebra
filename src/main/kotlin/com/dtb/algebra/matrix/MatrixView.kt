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

	override fun get(i: Int, j: Int): Double {
		if (i > this.width || j > this.height)
			throw IndexOutOfBoundsException()
		return parent[i + this.xOffset, j + this.yOffset]
	}
}