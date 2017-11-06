package name.zautner.vitaly.appthis.web

import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView

/**
  * Very simple error handler, which returns the client back home
  * in the event of some error.
  */
class HomeController extends ErrorController {

    @GetMapping(value = Array("/error")) def errorGet(): RedirectView = {
        new RedirectView("/login")
    }

    override def getErrorPath: String = {
        "/error"
    }
}