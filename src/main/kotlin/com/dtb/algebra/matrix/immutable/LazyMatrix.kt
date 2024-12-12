package com.dtb.algebra.matrix.immutable

class LazyMatrix(val width: Int, val height: Int, val calc: (Int, Int) -> Double): AbstractMatrix() {
	override fun width(): Int = width
	override fun height(): Int = height
	override fun get(i: Int, j: Int): Double = calc(i, j)
}