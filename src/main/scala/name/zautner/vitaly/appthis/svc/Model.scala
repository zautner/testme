package name.zautner.vitaly.appthis.svc

/**
  * Data Transfer Object keeper
  */
object Model {

    case class EndPointDTO(url: String, alias: String)

    case class UserDTO(account: String, endPoints: Iterable[EndPointDTO])

}
