package name.zautner.vitaly.appthis.web

import java.net.MalformedURLException
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

import name.zautner.vitaly.appthis.svc.Model.EndPointDTO
import name.zautner.vitaly.appthis.svc.proto.{EndPointService, UserService}
import name.zautner.vitaly.appthis.web.validation.InputValidator
import org.hibernate.{HibernateException, JDBCException}
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation._

/**
  * This controller serves end-point maintenance related REST calls
  */

@RestController
@RequestMapping(Array("/endpoint"))
class EndPointMaintenanceController @Inject()(endPointService: EndPointService,
                                              userService: UserService) {

    @RequestMapping(value = Array("/", ""), method = Array(RequestMethod.GET))
    @ResponseBody def listEndPoints(auth: Authentication,
                                    request: HttpServletRequest): Iterable[EndPointDTO] = {
        val protocol = getProtocol(request)
        endPointService.listEndpointsForUser(userService.currentUserId(auth), s"$protocol://${request.getServerName}:${request.getServerPort}")
    }

    private def getProtocol(request: HttpServletRequest) = {
        if (request.isSecure) "https" else "http"
    }

    @PostMapping(value = Array("/", ""))
    @throws[MalformedURLException]("Non-valid url")
    @ResponseBody def addEndPoint(@RequestParam(required = true, value = "url") url: String,
                                  auth: Authentication,
                                  request: HttpServletRequest): String = {
        InputValidator.checkUrl(url)
        val userId: Long = userService.currentUserId(auth)
        val protocol = getProtocol(request)
        s"$protocol://${request.getServerName}:${request.getServerPort}/${endPointService.createEndPointForUser(url, userId)}"
    }

    @PutMapping(value = Array("/", ""))
    @throws[IllegalArgumentException]("No enity is found")
    @ResponseBody def modifyEndPoint(@RequestParam(value = "url", required = true) url: String,
                                     @RequestParam(value = "alias", required = true) alias: String,
                                     auth: Authentication): String = {
        endPointService.modifyEndPointForUser(alias, userService.currentUserId(auth), url)
    }

    @DeleteMapping(value = Array("/", ""))
    @throws[IllegalArgumentException]("No enity is found")
    @ResponseBody def deleteEndPoint(@RequestParam(value = "alias", required = true) alias: String,
                                     auth: Authentication): Unit = {
        endPointService.deleteEndPointForUser(alias, userService.currentUserId(auth))
    }

    @ExceptionHandler(Array(classOf[MalformedURLException]))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    def badRequestHandler(e: MalformedURLException): String = {
        s"Wrong parameter passed<br/> <h3>${e.getMessage}</h3>"
    }

    @ExceptionHandler(Array(classOf[HibernateException], classOf[JDBCException]))
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    def dbRequestHandler(e: Exception): String = {
        s"Database inconsistency<br/> <h3>${e.getMessage}</h3>"

    }

    @ExceptionHandler(Array(classOf[RuntimeException]))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    def anotherRequestHandler(e: Exception): String = {
        s"Problem detected<br/> <h3>${e.getMessage}</h3>"
    }
}

