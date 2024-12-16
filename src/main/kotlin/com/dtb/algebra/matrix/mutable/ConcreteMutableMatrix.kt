package com.dtb.algebra.matrix.mutable

class ConcreteMutableMatrix(val width: Int, val height: Int, initializer: (Int, Int) -> Double): AbstractMutableMatrix() {
	val values = MutableList(width * height) { index -> initializer(index % width, index / width) }

	override fun width(): Int = this.width
	override fun height(): Int = this.height

	override fun get(i: Int, j: Int): Double = this.values[i + j * width]
	override fun set(i: Int, j: Int, value: Double) {
		this.values[i + j * width] = value
	}
}