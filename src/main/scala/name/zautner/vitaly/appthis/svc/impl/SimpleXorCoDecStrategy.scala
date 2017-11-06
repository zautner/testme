package name.zautner.vitaly.appthis.svc.impl

import name.zautner.vitaly.appthis.svc.proto.CoDecStrategy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
  * Implementation of the simplest [[CoDecStrategy]], which actually uses XOR
  * with hard-coded value (in both ways).
  */

@Service
class SimpleXorCoDecStrategy extends CoDecStrategy {
    private val logger = LoggerFactory.getLogger(getClass)
    private final val saltAndPepper: Long = 0xdeadbeefL

    override def code(id: Long): String = "%08X".format(id ^ saltAndPepper)

    override def decode(url: String): Long = {
        logger.info(s"decode($url) started")
        val interMediate = java.lang.Long.decode(s"0x$url")
        logger.info(s"decode($url) finished")
        interMediate ^ saltAndPepper
    }
}
