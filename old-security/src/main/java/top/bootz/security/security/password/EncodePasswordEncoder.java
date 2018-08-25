package top.bootz.security.security.password;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.yxt.common.util.APIUtil;

/**
 * The Class MyPasswordEncoder.
 */
@SessionAttributes("authorizationRequest")
public class EncodePasswordEncoder implements PasswordEncoder {

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.PasswordEncoder#encodePassword(java.lang.String, java.lang.Object)
	 */
	public String encodePassword(String rawPass, Object salt) {
		return APIUtil.encryptPassword(rawPass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.PasswordEncoder#isPasswordValid(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {

		return encodePassword(rawPass, salt).equals(encPass);
	}
}