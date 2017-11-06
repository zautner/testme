package name.zautner.vitaly.appthis.web

import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.view.RedirectView

/**
  * Very simple error handler controller, which returns the client back home
  * in the event of some error.
  */
@Controller class HomeController extends ErrorController {

    import org.springframework.web.bind.annotation.RequestMapping

    @RequestMapping(value = Array("/error")) def error: RedirectView = {
        new RedirectView("/login")
    }

    override def getErrorPath: String = {
        "/error"
    }
}