package com.dtb.algebra.matrix

import java.util.logging.Logger

open class ConcreteMatrix(val width: Int, val height: Int, initializer: (Int, Int) -> Double) : AbstractMatrix() {
	private val values: Array<Double> = Array(width * height) { initializer(it % width, it / width) }
	constructor(width: Int, height: Int): this(width, height, {_, _ -> 0.0 })

	override fun width(): Int  = this.width
	override fun height(): Int = this.height

	private operator fun get(i: Int): Double = values[i]
	override operator fun get(i: Int, j: Int): Double {
		if (j * width() + i >= this.values.size) {
			Logger
				.getLogger("Matrix")
				.severe("matrix[$i, $j] out of bounds for size [$width, $height]")
			throw IndexOutOfBoundsException()
		}
		return values[j * width() + i]
	}

	private operator fun set(i: Int, value: Double) { values[i] = value }
	private operator fun set(i: Int, j: Int, value: Double) { values[j * width + i] = value }
}