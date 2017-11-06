package name.zautner.vitaly.appthis.app

import org.springframework.boot.SpringApplication

/**
  * Spring boot enry point object definition.
  */
object OpaqueUrlBootRunner extends App {
    /**
      * Application's entry point function.
      *
      * @param args Command-line arguments.
      */
    override def main(args: Array[String]): Unit = {
        SpringApplication.run(classOf[OpaqueUrlApplication], args: _*)
    }
}
