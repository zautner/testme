package name.zautner.vitaly.appthis.web

import javax.inject.Inject

import name.zautner.vitaly.appthis.svc.Model.UserDTO
import name.zautner.vitaly.appthis.svc.proto.UserService
import name.zautner.vitaly.appthis.web.validation.InputValidator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.view.RedirectView

/**
  * This is a simplest user housekeeping controller.
  *
  * @param userService - user service bean
  */
@Controller
@RequestMapping(value = Array("/user"))
class UserMaintenanceController @Inject()(userService: UserService) {
    @GetMapping(value = Array("/", ""))
    @ResponseBody def listUsers(auth: Authentication): Iterable[UserDTO] = {
        userService.listUsers
    }

    @PostMapping(value = Array("/", ""))
    @ResponseBody def addUser(auth: Authentication,
                              @RequestParam(required = true, name = "account") account: String,
                              @RequestParam(required = true, name = "password") password: String): UserDTO = {
        InputValidator.checkUserPassword(password)
        InputValidator.checkUserName(account)
        userService.addUser(userService.currentUserId(auth), account, password)
    }

    @PutMapping(value = Array("/", ""))
    @ResponseBody def modifyPassword(auth: Authentication,
                                     @RequestParam(required = true, name = "account") account: String,
                                     @RequestParam(required = true, name = "password") password: String,
                                     @RequestParam(required = true, name = "new_password") newPassword: String): AnyRef = {
        InputValidator.checkUserPassword(newPassword)
        userService.modifyPassword(userService.currentUserAccount(auth),
            userService.currentUserId(auth),
            password,
            newPassword)
        new Object() {
            final val status = "200"
        }
    }

    @DeleteMapping(value = Array("/", ""))
    def deleteMyUser(auth: Authentication): RedirectView = {
        userService.deleteCurrentUser(userService.currentUserId(auth))
        new RedirectView("/login")
    }


}
