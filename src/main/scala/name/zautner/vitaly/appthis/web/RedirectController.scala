package name.zautner.vitaly.appthis.web

import javax.inject.Inject

import name.zautner.vitaly.appthis.svc.proto.EndPointService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping}
import org.springframework.web.servlet.view.RedirectView

/**
  * This is a main functional controller of "OpaqueUrl" service.
  *
  * @param endPointService - the url entry-point service's bean
  */
@Controller
@RequestMapping(value = Array("/"))
class RedirectController @Inject()(endPointService: EndPointService) {

    /**
      * Translate alias string to the real url (for the current user) and redirect
      *
      * @param alias The string, which comes from the client (browser)
      * @return RedirectView object, which will take the user to the re-directed URL
      */
    @RequestMapping(path = Array("{alias:[0-9a-hA-H]+}"))
    def reqirectToUrlPage(@PathVariable alias: String): RedirectView = new RedirectView(endPointService.translateAlias(alias))

    @RequestMapping(path = Array("{alias:home.+}"))
    def home(@PathVariable alias: String): String = "testme.html"
}
