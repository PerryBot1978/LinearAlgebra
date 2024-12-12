package com.dtb.algebra.matrix.immutable

import com.dtb.algebra.matrix.immutable.Matrix
import com.dtb.algebra.matrix.immutable.ConcreteMatrix
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

import com.dtb.algebra.matrix.immutable.Matrix.Companion.times
import kotlin.random.Random
import kotlin.test.assertNotEquals

object LazyMatrixTest {
	@Test
	fun constructor1() {
		for (i in 1..<20) {
			for (j in 1..<20) {
				val matrix = Matrix.lazy(i, j)
				assertEquals(matrix.width(), i)
				assertEquals(matrix.height(), j)

				for (i2 in 0..<i)
					for (j2 in 0..<j)
						assertEquals(matrix[i2, j2], 0.0)
			}
		}

		Matrix.lazy(1000, 1000)
	}
	@Test
	fun constructor2() {
		for (i in 1..<20)
			for (j in 1..<20)
				for (k in 1..<5) {
					val matrix = Matrix.new(i, j) { i, j -> (i * j * k).toDouble() }
					assertEquals(matrix.width(), i)
					assertEquals(matrix.height(), j)

					for (i2 in 0..<i)
						for (j2 in 0..<j)
							assertEquals(matrix[i2, j2], (i2 * j2 * k).toDouble())
				}

		Matrix.lazy(1000, 1000) { i, j -> (i + j).toDouble() }
	}

	@Test
	fun plus() {
		val matrix1 = Matrix.lazy(3, 3) { _, _ -> 1.0 }
		val matrix2 = Matrix.lazy(3, 3) { _, _ -> 2.0 }
		val sum     = Matrix.lazy(3, 3) { _, _ -> 3.0 }
		assertEquals(sum, matrix1 + matrix2)

		val matrix3 = Matrix.lazy(3, 2) { _, _ -> 3.0 }
		assertThrows<IllegalArgumentException> { matrix1 + matrix3 }
		assertThrows<IllegalArgumentException> { matrix3 + matrix2 }
	}
	@Test
	fun minus() {
		val matrix1 = Matrix.lazy(3, 3) { _, _ -> 1.0 }
		val matrix2 = Matrix.lazy(3, 3) { _, _ -> 2.0 }
		val diff1   = Matrix.lazy(3, 3) { _, _ -> -1.0 }
		val diff2   = Matrix.lazy(3, 3) { _, _ -> 1.0 }
		assertEquals(diff1, matrix1 - matrix2)
		assertEquals(diff2, matrix2 - matrix1)

		val matrix3 = Matrix.lazy(3, 2) { _, _ -> 3.0 }
		assertThrows<IllegalArgumentException> { matrix1 - matrix3 }
		assertThrows<IllegalArgumentException> { matrix3 - matrix2 }
	}

	@Test
	fun scalarTimes() {
		val matrix = Matrix.lazy(3, 3) { _, _ -> 1.0 }

		val double = Matrix.lazy(3, 3) { _, _ -> 2.0 }
		assertEquals(double, matrix * 2)
		assertEquals(double, 2 * matrix)

		val triple = Matrix.lazy(3, 3) { _, _ -> 3.0 }
		assertEquals(triple, matrix * 3)
		assertEquals(triple, 3 * matrix)

		val negative = Matrix.lazy(3, 3) { _, _ -> -1.0 }
		assertEquals(negative, matrix * -1)
		assertEquals(negative, -1 * matrix)
	}
	@Test
	fun div() {
		val matrix = Matrix.lazy(3, 3) { _, _ -> 1.0 }

		val half = Matrix.lazy(3, 3) { _, _ -> 0.5 }
		assertEquals(half, matrix / 2)

		val quarter = Matrix.lazy(3, 3) { _, _ -> 0.25 }
		assertEquals(quarter, matrix / 4)

		val negative = Matrix.lazy(3, 3) { _, _ -> -1.0 }
		assertEquals(negative, matrix / -1)
	}

	@Test
	fun times() {
		val matrix1 = Matrix.lazy(2, 2) { i, j -> i - j + 2.0 }
		val square1 = Matrix.of("7,12;4,7")
		assertEquals(square1, matrix1 * matrix1)

		val matrix2 = Matrix.lazy(2, 2) { i, j -> i * j + 1.0 }
		val product1 = Matrix.of("5,8;3,5")
		assertEquals(product1, matrix1 * matrix2)

		val matrix3 = Matrix.lazy(3, 2) { i, j -> i + j + 1.0 }
		val matrix4 = Matrix.lazy(2, 3) { i, j -> i + j + 1.0 }
		val product2 = Matrix.of("14,20;20,29")
		assertEquals(product2, matrix3 * matrix4)
	}

	@Test
	fun minor() {
		val matrix1 = Matrix.of("2,3,4;4,3,2;2,3,4")
		assertEquals(Matrix.of("2,4;2,4"), matrix1.minor(1, 1))
		assertEquals(Matrix.of("4,3;2,3"), matrix1.minor(2, 0))

		val matrix2 = Matrix.lazy(3, 3)
		assertNotEquals(matrix1.minor(1, 1), matrix2.minor(1, 1))
	}

	@Test
	fun cofactor() {
		val matrix1 = Matrix.of("2")
		assertEquals(Matrix.of("1"), matrix1.cofactor())

		val matrix2 = Matrix.of("1,2;3,4")
		assertEquals(Matrix.of("4,-3;-2,1"), matrix2.cofactor())

		val matrix3 = Matrix.of("1,2,3;4,5,6;7,8,9")
		assertEquals(Matrix.of("-3,6,-3;6,-12,6;-3,6,-3"), matrix3.cofactor())
	}

	@Test
	fun adjunct() {
		val matrix1 = Matrix.of("2")
		assertEquals(Matrix.of("1"), matrix1.adjunct())

		val matrix2 = Matrix.of("1,2;3,4")
		assertEquals(Matrix.of("4,-2;-3,1"), matrix2.adjunct())

		val matrix3 = Matrix.of("1,2,3;4,5,6;7,8,9")
		assertEquals(Matrix.of("-3,6,-3;6,-12,6;-3,6,-3"), matrix3.adjunct())
	}

	@Test
	fun inverse() {
		val matrix1 = Matrix.of("2")
		assertEquals(Matrix.of("0.5"), matrix1.inverse())

		val matrix2 = Matrix.of("1,2;3,4")
		assertEquals(Matrix.of("-2,1;1.5,-0.5"), matrix2.inverse())

		val matrix3 = Matrix.of("1,2,3;4,5,6;7,8,9")
		assertThrows<IllegalArgumentException> { matrix3.inverse() }
	}

	@Test
	fun determinate() {
		val matrix1 = Matrix.of("1,4;5,7")
		assertEquals(-13.0, matrix1.determinate())

		val matrix2 = Matrix.of("3,5,2;4,1,-1;8,7,2")
		assertEquals(-13.0, matrix2.determinate())

		val matrix3 = Matrix.of("5,0,2,0;4,7,8,2;1,8,2,1;2,9,1,2")
		assertEquals(-247.0, matrix3.determinate())
	}

	@Test
	fun hashcode() {
		for (i in 1..<5)
			for (j in 1..<5)
				assertEquals(Matrix.lazy(i, j).hashCode(), 0)

		for (i in 1..<20)
			for (j in 1..<20) {
				val matrix = ConcreteMatrix(i, j) { _, _ -> Random.nextDouble() }
				assertEquals(matrix.hashCode(), matrix.hashCode())
			}
	}
}