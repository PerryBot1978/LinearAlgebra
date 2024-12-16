package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.LazyMatrix
import com.dtb.algebra.matrix.Matrix

class VerticalMatrixMerge(
	val left: Matrix,
	val right: Matrix
): LazyMatrix(
	left.width(),
	right.height() + left.height(),
	{ i, j ->
		if (j < left.height()) left[i, j]
		else right[i, j - left.height()]
	}
)