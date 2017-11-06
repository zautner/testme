package name.zautner.vitaly.appthis.web.validation

import java.net.MalformedURLException

import org.apache.commons.validator.routines.UrlValidator

/**
  * This is a custom simple validator for input fields:
  * we check both URL and end-pont name
  */
object InputValidator {

    @throws[MalformedURLException]("Non-valid url")
    def checkUrl(url: String): Unit = if (!new UrlValidator().isValid(url)) {
        throw new MalformedURLException(s"Non-valid URL string: `$url`")
    }

    def checkUserName(name: String): Unit = if (name == null || name.isEmpty || name.length > 30) {
        throw new InputValidationException(s"Non-valid account name: `$name`")
    }

    def checkUserPassword(password: String): Unit = if (
        password == null
          || password.length < 3
          || password.length > 30
          || password.contentEquals("qwerty")
          || password.startsWith("123456")
    ) {
        throw new InputValidationException(s"Non-valid password: `$password`")
    }

    case class InputValidationException(data: AnyRef) extends Exception(data.toString)

}
