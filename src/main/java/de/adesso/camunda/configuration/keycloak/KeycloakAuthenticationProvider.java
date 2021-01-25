package de.adesso.camunda.configuration.keycloak;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * OAuth2 Authentication Provider for usage with Keycloak and
 * KeycloakIdentityProviderPlugin.
 */
public class KeycloakAuthenticationProvider extends ContainerBasedAuthenticationProvider {

	@Override
	public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {

		// Extract user-name-attribute of the OAuth2 token
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof OAuth2AuthenticationToken)
				|| !(authentication.getPrincipal() instanceof OidcUser)) {
			return AuthenticationResult.unsuccessful();
		}
		String userId = ((OidcUser) authentication.getPrincipal()).getName();
		if (StringUtils.isEmpty(userId)) {
			return AuthenticationResult.unsuccessful();
		}

		// Authentication successful
		AuthenticationResult authenticationResult = new AuthenticationResult(userId, true);
		authenticationResult.setGroups(getUserGroups(userId, engine));

		return authenticationResult;
	}

	private List<String> getUserGroups(String userId, ProcessEngine engine) {
		// query groups using KeycloakIdentityProvider plugin
		return engine.getIdentityService().createGroupQuery().groupMember(userId).list().stream().map(Group::getId)
				.collect(Collectors.toList());
	}

}