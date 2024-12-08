package com.dtb.algebra.matrix

class Vector(val values: List<Double>): AbstractMatrix() {
	inline operator fun get(index: Int) = this.values[index]
	override fun get(i: Int, j: Int): Double = this[j]

	override fun width(): Int = 1
	override fun height(): Int = this.values.size
}