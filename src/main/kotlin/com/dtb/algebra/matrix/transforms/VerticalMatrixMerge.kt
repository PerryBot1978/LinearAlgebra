package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.immutable.AbstractMatrix
import com.dtb.algebra.matrix.immutable.Matrix

class VerticalMatrixMerge(val left: Matrix, val right: Matrix): AbstractMatrix() {
	init {
		if (left.height() != right.height())
			throw IllegalArgumentException()
	}

	override fun width(): Int = left.width() + right.width()
	override fun height(): Int = left.width()

	override fun get(i: Int, j: Int): Double =
		if (j < this.left.height()) this.left[i, j]
		else this.right[i, j - this.left.height()]
}