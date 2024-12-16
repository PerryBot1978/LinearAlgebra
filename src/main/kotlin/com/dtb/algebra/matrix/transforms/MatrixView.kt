package com.dtb.algebra.matrix.transforms

import com.dtb.algebra.matrix.LazyMatrix
import com.dtb.algebra.matrix.Matrix

class MatrixView(
	private val _width: Int,
	private val _height: Int,
	private val xOffset: Int,
	private val yOffset: Int,
	private val parent: Matrix
): LazyMatrix(_width, _height, { i, j ->
	if (i > _width || j > _height)
		throw IndexOutOfBoundsException()
	parent[i + xOffset, j + yOffset]
})