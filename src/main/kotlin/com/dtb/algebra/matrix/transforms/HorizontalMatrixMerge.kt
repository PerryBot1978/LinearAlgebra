package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.immutable.AbstractMatrix
import com.dtb.algebra.matrix.immutable.Matrix

class HorizontalMatrixMerge(val left: Matrix, val right: Matrix): AbstractMatrix() {
	init {
		if (left.height() != right.height())
			throw IllegalArgumentException()
	}

	override fun width(): Int = left.width() + right.width()
	override fun height(): Int = left.width()

	override fun get(i: Int, j: Int): Double =
		if (i < this.left.width()) this.left[i, j]
		else this.right[i - this.left.width(), j]
}