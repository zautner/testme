package name.zautner.vitaly.appthis.svc.proto

/**
  * Strategy Design Pattern implementation for Url Coder-Decoder function
  */
trait CoDecStrategy {
    def code(id: Long): String

    def decode(url: String): Long
}
