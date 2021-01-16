package fusion.playground.views.security;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Route("server-security-info")
@PageTitle("Security info")
public class SecurityInfoView extends VerticalLayout {

    public SecurityInfoView() {

        add(new H3("Security overview"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt) {
            addJwtPrincipal((Jwt) principal);
        }
        else {
            add(new H4("Unknown Security Principal:"));
            add(principal.toString());

            // we only add the security credentials if they are not Jwt,
            // because for Jwt they are the same as the principal
            add(new H4("Security Credentials:"));
            Object credentials = authentication.getCredentials();
            add(credentials.toString());
        }

        add(new H4("Granted Authorities:"));
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        authorities.forEach(grantedAuthority -> add(new Span("grantedAuthority: " + grantedAuthority.toString())));
    }

    private void addJwtPrincipal(Jwt jwtPrincipal) {

        add(new H4("Jwt Security Principal:"));
        add(new Span ("jwtPrincipal.getId(): " + jwtPrincipal.getId()));
        add(new Span ("jwtPrincipal.getSubject(): " + jwtPrincipal.getSubject()));
        add(new Span ("jwtPrincipal.getTokenValue(): " + jwtPrincipal.getTokenValue()));
        add(new Span ("jwtPrincipal.getIssuer(): " + jwtPrincipal.getIssuer()));
        add(new Span ("jwtPrincipal.getIssuedAt(): " + jwtPrincipal.getIssuedAt()));
        add(new Span ("jwtPrincipal.getExpiresAt(): " + jwtPrincipal.getExpiresAt()));

        add(new H5("Headers"));
        Map<String, Object> headers = jwtPrincipal.getHeaders();
        headers.keySet().forEach(key -> add(new Span("jwtPrincipal.getHeaders().get(" + key + "): " + headers.get(key).toString())));
        add(new H5("Audience"));
        List<String> audience = jwtPrincipal.getAudience();
        audience.forEach(item -> add(new Span("jwtPrincipal.getAudience() - item: " + item )));
        add(new H5("Claims"));
        Map<String, Object> claims = jwtPrincipal.getClaims();
        claims.keySet().forEach(key -> add(new Span("jwtPrincipal.getClaims().get(" + key + "): " + claims.get(key).toString())));
    }
}
