package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.LazyMatrix
import com.dtb.algebra.matrix.Matrix

class MatrixMinor(
	private val excludeI: Int,
	private val excludeJ: Int,
	private val parent: Matrix
): LazyMatrix(parent.width() - 1, parent.height() - 1, { i, j ->
	if (i > parent.width() - 1 || j > parent.height() - 1)
		throw IndexOutOfBoundsException()

	val newI = if (i >= excludeI) i + 1 else i
	val newJ = if (j >= excludeJ) j + 1 else j

	parent[newI, newJ]
})