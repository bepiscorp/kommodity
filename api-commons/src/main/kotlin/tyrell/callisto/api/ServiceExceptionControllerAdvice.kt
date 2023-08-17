package tyrell.callisto.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import tyrell.callisto.api.enums.httpStatus
import tyrell.callisto.base.StandardResponseCode
import tyrell.callisto.base.exception.ServiceException
import tyrell.callisto.base.messaging.model.ResponseData

@ControllerAdvice
internal class ServiceExceptionControllerAdvice {

    @ExceptionHandler
    fun handleException(exception: Throwable): ResponseEntity<ResponseData> = handleAnyException(exception)

    private fun handleAnyException(exception: Throwable): ResponseEntity<ResponseData> {
        if (exception is ServiceException) return handleServiceException(exception)

        val wrappedException = ServiceException(StandardResponseCode.STD0003_ERROR, exception)
        return handleServiceException(wrappedException)
    }

    private fun handleServiceException(exception: ServiceException): ResponseEntity<ResponseData> {
        val responseData: ResponseData = exception.data
        val responseStatus: HttpStatus = responseData.reposeType.httpStatus
        return ResponseEntity<ResponseData>(responseData, responseStatus)
    }
}
