package argubaydulin.study.delivery.core.domain.model

import argubaydulin.study.delivery.libs.ddd.ValueObject
import argubaydulin.study.delivery.libs.errs.Result
import argubaydulin.study.delivery.libs.errs.Error
import argubaydulin.study.delivery.libs.errs.GeneralErrors
import kotlin.math.abs


class Location private constructor(
    val x: Int,
    val y: Int
) : ValueObject<Location>() {

    fun distanceTo(target: Location): Result<Int, Error> {
        if (this == target) {
            return Result.success(0)
        }
        val distance = abs(x - target.x) + abs(y - target.y)
        return Result.success(distance)
    }

    override fun equalityComponents(): Iterable<Any> {
        return listOf(x, y)
    }

    companion object {
        const val MIN_COORDINATE = 1
        const val MAX_COORDINATE = 10

        fun create(x: Int, y: Int): Result<Location, Error> {
            if (x < MIN_COORDINATE || x > MAX_COORDINATE) {
                return Result.failure(GeneralErrors.valueIsOutOfRange("x", x, MIN_COORDINATE, MAX_COORDINATE))
            }
            if (y < MIN_COORDINATE || y > MAX_COORDINATE) {
                return Result.failure(GeneralErrors.valueIsOutOfRange("y", y, MIN_COORDINATE, MAX_COORDINATE))
            }
            return Result.success(Location(x, y))
        }

    }

}