package org.blockchain.wallet.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blockchain.wallet.base.BaseResponse;
import org.blockchain.wallet.base.ErrorResponse;
import org.blockchain.wallet.constant.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author housirvip
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class MyExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse handlerRuntimeException(RuntimeException ex) {

        if (ex.getMessage() == null || ex.getMessage().isEmpty()) {
            return new ErrorResponse(ErrorMessage.SERVICE_EXCEPTION);
        }

        return new ErrorResponse(ex.getMessage());
    }
}
