package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.AbstractMatrix
import com.dtb.algebra.matrix.Matrix

class MatrixMinor(val excludeI: Int, val excludeJ: Int, val parent: Matrix): AbstractMatrix() {
	override fun width(): Int = parent.width() - 1
	override fun height(): Int = parent.height() - 1

	override fun get(i: Int, j: Int): Double {
		if (i > this.width() || j > this.height())
			throw IndexOutOfBoundsException()

		val newI = if (i >= this.excludeI) i + 1 else i
		val newJ = if (j >= this.excludeJ) j + 1 else j

//		println("($i, $j) & ($excludeI, $excludeJ) => ($newI, $newJ)")
		return parent[newI, newJ]
	}
}