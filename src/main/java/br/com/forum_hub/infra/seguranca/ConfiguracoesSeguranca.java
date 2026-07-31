package br.com.forum_hub.infra.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracoesSeguranca {

    private final FiltroTokenAcesso filtro;

    public ConfiguracoesSeguranca(FiltroTokenAcesso filtro) {
        this.filtro = filtro;
    }

    @Bean
    public PasswordEncoder encriptador() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers(
                                    "/login",
                                    "/atualizar-token",
                                    "registrar",
                                    "/verificar-conta"
                            ).permitAll();
                            req.requestMatchers(HttpMethod.GET, "cursos").permitAll();
                            req.requestMatchers(HttpMethod.GET, "topicos/**").permitAll();

                            req.requestMatchers(HttpMethod.POST, "topicos").hasRole("ESTUDANTE");
                            req.requestMatchers(HttpMethod.PUT, "topicos").hasRole("ESTUDANTE");
                            req.requestMatchers(HttpMethod.DELETE, "topicos").hasRole("ESTUDANTE");

                            req.requestMatchers(HttpMethod.PATCH, "topicos/**").hasRole("MODERADOR");

                            req.requestMatchers(HttpMethod.PATCH, "adicionar-perfil/**").hasRole("ADMIN");

                            req.anyRequest().authenticated();
                        })
                .sessionManagement(
                sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filtro, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy hierarquiaPerfil() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("MODERADOR")
                .role("MODERADOR").implies("ESTUDANTE", "INSTRUTOR")
                .build();
    }

}
