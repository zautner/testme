package name.zautner.vitaly.appthis.app

import javax.sql.DataSource

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.hibernate.SessionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/**
  * Spring boot app configuration class
  */

@SpringBootApplication(scanBasePackages = Array("name.zautner.vitaly.appthis"))
@EntityScan(basePackages = Array("name.zautner.vitaly.appthis.dao", "name.zautner.vitaly.appthis.dao.model"))
class OpaqueUrlApplication {
    // Hibernate session management bean
    @Bean
    def getSessionFactory(dataSource: DataSource): SessionFactory = new LocalSessionFactoryBuilder(dataSource)
      .scanPackages("name.zautner.vitaly.appthis.dao")
      .buildSessionFactory()

    // Support Scala classes for converting objects to JSON
    @Bean
    def getMessageConverter: HttpMessageConverter[Object] = {
        val mapper = new ObjectMapper()
        mapper.registerModule(DefaultScalaModule)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        new MappingJackson2HttpMessageConverter(mapper)
    }

    // Password encoder. The simplest one,
    // to be on the safe side when I automatically fill-up database with SQL script `import.sql`
    @Bean
    def passwordEncoder(): PasswordEncoder = new PasswordEncoder {
        override def encode(rawPassword: CharSequence): String = rawPassword.toString

        override def matches(rawPassword: CharSequence, encodedPassword: String): Boolean = encodedPassword.contentEquals(rawPassword)
    }

    // Support for my extended User Details with the current User Long ID
    @Bean
    def getUserDetailService: UserDetailsService = new OpaqueUrlUserDetailsService()
}
