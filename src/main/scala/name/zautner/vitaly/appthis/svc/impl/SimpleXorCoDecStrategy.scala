package name.zautner.vitaly.appthis.svc.impl

import name.zautner.vitaly.appthis.svc.proto.CoDecStrategy
import org.springframework.stereotype.Service

/**
  * Implementation of the simplest [[CoDecStrategy]], which actually uses XOR
  * with hard-coded value (in both ways).
  */

@Service
class SimpleXorCoDecStrategy extends CoDecStrategy {
    private final val saltAndPepper: Long = 0xdeadbeefL

    override def code(id: Long): String = "%08X".format(id ^ saltAndPepper)

    override def decode(url: String): Long = {
        val interMediate = java.lang.Long.decode(s"0x$url")
        interMediate ^ saltAndPepper
    }
}
