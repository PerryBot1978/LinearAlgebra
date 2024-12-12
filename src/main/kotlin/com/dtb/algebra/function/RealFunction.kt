package com.dtb.algebra.function

import com.dtb.algebra.matrix.immutable.Vector

interface RealFunction: (Double) -> Double {
	fun toVector(): Vector
}
