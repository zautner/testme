package name.zautner.vitaly.appthis.web.validation

import java.net.MalformedURLException

import name.zautner.vitaly.appthis.web.validation.InputValidator.InputValidationException
import org.hibernate.{HibernateException, JDBCException}
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler, ResponseStatus}

@ControllerAdvice(value = Array("name.zautner.vitaly.appthis.web"))
class ExceptionHandlerAdvice {
    @ExceptionHandler(Array(classOf[MalformedURLException], classOf[InputValidationException], classOf[NumberFormatException]))
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    def badEntityHandler(e: Exception): String = {
        s"Wrong parameter passed<br/> <h3>${e.getMessage}</h3>"
    }

    @ExceptionHandler(Array(classOf[HibernateException], classOf[JDBCException]))
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    def badDbRequestHandler(e: Exception): String = {
        s"Database inconsistency<br/> <h3>${e.getMessage}</h3>"

    }

    @ExceptionHandler(Array(classOf[IllegalArgumentException]))
    @ResponseStatus(HttpStatus.CONFLICT)
    def badIdPassedHandler(e: Exception): String = {
        s"Problem detected<br/> <h3>${e.getMessage}</h3>"
    }

    @ExceptionHandler(Array(classOf[RuntimeException]))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    def anotherRequestHandler(e: Exception): String = {
        s"Problem detected<br/> <h3>${e.getMessage}</h3>"
    }

}
