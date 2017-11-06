package name.zautner.vitaly.appthis.app

import javax.inject.Inject
import javax.sql.DataSource

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

/**
  * Spring security configuration
  *
  * @param encoder
  * @param dataSource
  * @param udService
  */

@Configuration
@EnableWebSecurity
class OpaqueUrlSecurityCfg @Inject()(encoder: PasswordEncoder,
                                     dataSource: DataSource,
                                     udService: UserDetailsService) extends WebSecurityConfigurerAdapter() {

    override def configure(auth: AuthenticationManagerBuilder): Unit = {
        auth.userDetailsService(udService)
        auth.jdbcAuthentication()
          .dataSource(dataSource)
          .passwordEncoder(encoder)

    }

    override def configure(http: HttpSecurity): Unit = {
        http.authorizeRequests()
          .antMatchers("/home**").authenticated()
          .antMatchers("/endpoint/**").authenticated()
          .antMatchers("/login").permitAll()
          .antMatchers("/login/**").permitAll()
          .antMatchers("/user/**").authenticated()
          .and().csrf().disable().formLogin()
          .defaultSuccessUrl("/home.htmlx")
          .usernameParameter("username")
          .passwordParameter("password")
          .and().logout()
    }
}
