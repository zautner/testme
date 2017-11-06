package name.zautner.vitaly.appthis.web.validation

import java.net.MalformedURLException

import org.apache.commons.validator.routines.UrlValidator

/**
  * This is a custom simple validator for input fields:
  * we check both URL and end-pont name
  */
object InputValidator {
    final private val NAME_MAXLEN = 255

    @throws[IllegalArgumentException]("Non-valid name")
    @throws[MalformedURLException]("Non-valid url")
    def checkBoth(url: String, name: String): Unit = {
        checkUrl(url)
        checkName(name)
    }

    @throws[MalformedURLException]("Non-valid url")
    def checkUrl(url: String): Unit = if (!new UrlValidator().isValid(url)) {
        throw new MalformedURLException(s"Non-valid URL string: `$url`")
    }

    @throws[IllegalArgumentException]("Non-valid name")
    def checkName(name: String): Unit = if (name.nonEmpty && name.length < NAME_MAXLEN) throw new IllegalArgumentException(s"$name")
}
