package com.dtb.algebra.function

import com.dtb.algebra.matrix.Vector

interface RealFunction: (Double) -> Double {
	fun toVector(): Vector
}
