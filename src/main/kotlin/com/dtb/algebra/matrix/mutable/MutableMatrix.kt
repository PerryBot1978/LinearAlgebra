package com.dtb.algebra.matrix.mutable

import com.dtb.algebra.matrix.immutable.Matrix

interface MutableMatrix: Matrix {
	operator fun set(i: Int, j: Int, value: Double)
}