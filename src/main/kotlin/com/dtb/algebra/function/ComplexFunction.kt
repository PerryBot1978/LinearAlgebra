package com.dtb.algebra.function

import com.dtb.algebra.matrix.Vector
import com.dtb.algebra.utils.Complex

interface ComplexFunction: (Complex) -> Complex {
	fun toVector(): Vector
}