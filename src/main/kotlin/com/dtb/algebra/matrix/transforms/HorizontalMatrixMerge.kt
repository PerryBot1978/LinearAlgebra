package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.LazyMatrix
import com.dtb.algebra.matrix.Matrix

class HorizontalMatrixMerge(
	private val left: Matrix,
	private val right: Matrix
): LazyMatrix(
	left.width() + right.width(),
	left.height(),
	{ i, j ->
		if (i < left.width()) left[i, j]
		else right[i - left.width(), j]
	}
)