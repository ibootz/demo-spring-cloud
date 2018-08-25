package top.bootz.security.security.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.yxt.common.exception.ApiException;
import com.yxt.lecai.entity.mysql.user.OrgUser;
import com.yxt.lecai.repo.mysql.orguser.OrgUserRepository;

@Service
@SessionAttributes("authorizationRequest")
public class UserService implements UserDetailsService {

	@Autowired
	private OrgUserRepository orgUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		OrgUser orguser = orgUserRepository.findUserByOauth(username);
		if (orguser == null) {
            throw new ApiException("apis.user.error.loginFailed");
        }
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		GrantedAuthorityImpl auth = new GrantedAuthorityImpl("ROLE_USER");
		auths.add(auth);
		User user = new User(orguser.getPid(), orguser.getPassword(), true, true, true, true, auths);
		return user;
	}

}
