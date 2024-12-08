package com.dtb.algebra.matrix.memoize

import com.dtb.algebra.matrix.Matrix

class MemoizeMinor(val f: (Int, Int) -> Matrix): (Int, Int) -> Matrix {
	private val values = mutableMapOf<Pair<Int, Int>, Matrix>()
	override fun invoke(i: Int, j: Int): Matrix {
		synchronized(values) {
			return values.getOrPut(Pair(i, j)) { f(i, j) }
		}
	}
}

fun ((Int, Int) -> Matrix).memoize() = MemoizeMinor(this)