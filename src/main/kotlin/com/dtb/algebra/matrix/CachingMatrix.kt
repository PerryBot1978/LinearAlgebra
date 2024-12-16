package com.dtb.algebra.matrix

class CachingMatrix(val width: Int, val height: Int, val calc: (Int, Int) -> Double): AbstractMatrix() {
	private val values = Array<Double?>(width * height) { index -> calc(index % width, index / width)}

	constructor(width: Int, height: Int): this(width, height, {_, _ -> 0.0 })

	override fun width(): Int = width
	override fun height(): Int = height
	override fun get(i: Int, j: Int): Double {
		if (values[i + j * width] == null)
			values[i + j * width] = calc(i, j)
		return values[i + j * width]!!
	}
}