package argubaydulin.study.delivery.core.domain.model.courier

import argubaydulin.study.delivery.libs.ddd.BaseEntity
import argubaydulin.study.delivery.libs.errs.Error
import argubaydulin.study.delivery.libs.errs.GeneralErrors
import argubaydulin.study.delivery.libs.errs.Result
import argubaydulin.study.delivery.libs.errs.UnitResult
import java.util.*

class StoragePlace private constructor(
    val id: UUID,
    val name: String,
    val totalVolume: Int,
    var orderId: UUID?,
) : BaseEntity<UUID>(id) {

    fun canStore(volume: Int): UnitResult<Error> {
        if (isOccupied()) {
            return UnitResult.failure(storageIsOccupied())
        }
        if (volume > totalVolume) {
            return UnitResult.failure(valueIsTooBig(volume, totalVolume))
        }
        return UnitResult.success()
    }

    fun store(orderId: UUID, volume: Int): UnitResult<Error> {
        val canStore = canStore(volume)
        if (canStore.isFailure) {
            return UnitResult.failure(canStore.error)
        }
        this.orderId = orderId
        return UnitResult.success()
    }

    fun clear(orderId: UUID): UnitResult<Error> {
        if (this.orderId != orderId) {
            return UnitResult.failure(
                GeneralErrors.valueIsInvalid(
                    "orderId",
                    "Can't clear $orderId from storage place because storage place stores ${this.orderId}"
                )
            )
        }
        this.orderId = null
        return UnitResult.success()
    }

    private fun isOccupied(): Boolean = orderId != null

    companion object {

        fun create(id: UUID, name: String, totalVolume: Int, orderId: UUID? = null): Result<StoragePlace, Error> {

            if (totalVolume <= 0) {
                return Result.failure(
                    GeneralErrors.valueIsInvalid("totalVolume", "must be greater than 0")
                )
            }
            if (name.isBlank()) {
                return Result.failure(
                    GeneralErrors.valueIsInvalid("name", "must be not empty0")
                )
            }
            return Result.success(StoragePlace(id, name, totalVolume, orderId))
        }

        private fun valueIsTooBig(volume: Int, totalVolume: Int): Error {
            return Error.of(
                "value.is.too.big.for.current.storage",
                "The value $volume exceeds storage's total volume of $totalVolume"
            )
        }

        private fun storageIsOccupied(): Error {
            return Error.of(
                "storage.is.occupied",
                "Current storage is already occupied"
            )
        }
    }

}
