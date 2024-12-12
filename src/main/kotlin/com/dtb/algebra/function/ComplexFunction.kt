package com.dtb.algebra.function

import com.dtb.algebra.matrix.immutable.Vector
import com.dtb.algebra.utils.Complex

interface ComplexFunction: (Complex) -> Complex {
	fun toVector(): Vector
}