package top.bootz.old_security.security.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;



/**
 * The Class MyUsernamePasswordAuthenticationFilter.
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	// 用户名
	/** The Constant SPRING_SECURITY_FORM_USERNAME_KEY. */
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "j_username";
	// 密码
	/** The Constant SPRING_SECURITY_FORM_PASSWORD_KEY. */
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "j_password";
	// 需要回调的URL 自定义参数
	/** The Constant SPRING_SECURITY_FORM_REDERICT_KEY. */
	public static final String SPRING_SECURITY_FORM_REDERICT_KEY = "spring-security-redirect";

	/**
	 * The Constant SPRING_SECURITY_LAST_USERNAME_KEY.
	 *
	 * @deprecated If you want to retain the username, cache it in a customized
	 *             {@code AuthenticationFailureHandler}
	 */
	@Deprecated
	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";

	/** The username parameter. */
	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

	/** The password parameter. */
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	/** The redirect parameter. */
	private String redirectParameter = SPRING_SECURITY_FORM_REDERICT_KEY;

	/** The post only. */
	private boolean postOnly = true;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * Instantiates a new my username password authentication filter.
	 */
	public CustomUsernamePasswordAuthenticationFilter() {
		super();
	}

	// ~ Methods
	// ========================================================================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * UsernamePasswordAuthenticationFilter
	 * #attemptAuthentication(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {		
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		String redirectUrl = obtainRedercitUrl(request);
		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		// 自定义回调URL，若存在则放入Session
		if (redirectUrl != null && !"".equals(redirectUrl)) {
			request.getSession().setAttribute("callCustomRediretUrl", redirectUrl);
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * Enables subclasses to override the composition of the password, such as
	 * by including additional values and a separator.
	 * <p>
	 * This might be used for example if a postcode/zipcode was required in
	 * addition to the password. A delimiter such as a pipe (|) should be used
	 * to separate the password and extended value(s). The
	 * <code>AuthenticationDao</code> will need to generate the expected
	 * password in a corresponding manner.
	 * </p>
	 * 
	 * @param request
	 *            so that request attributes can be retrieved
	 * 
	 * @return the password that will be presented in the
	 *         <code>Authentication</code> request token to the
	 *         <code>AuthenticationManager</code>
	 */
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	/**
	 * Enables subclasses to override the composition of the username, such as
	 * by including additional values and a separator.
	 * 
	 * @param request
	 *            so that request attributes can be retrieved
	 * 
	 * @return the username that will be presented in the
	 *         <code>Authentication</code> request token to the
	 *         <code>AuthenticationManager</code>
	 */
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	/**
	 * Obtain redercit url.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 */
	protected String obtainRedercitUrl(HttpServletRequest request) {
		return request.getParameter(redirectParameter);
	}

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 * 
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */
	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from
	 * the login request.
	 * 
	 * @param usernameParameter
	 *            the parameter name. Defaults to "j_username".
	 */
	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	/**
	 * Sets the parameter name which will be used to obtain the password from
	 * the login request..
	 * 
	 * @param passwordParameter
	 *            the parameter name. Defaults to "j_password".
	 */
	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter.
	 * If set to true, and an authentication request is received which is not a
	 * POST request, an exception will be raised immediately and authentication
	 * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
	 * will be called as if handling a failed authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 *
	 * @param postOnly
	 *            the new post only
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

}