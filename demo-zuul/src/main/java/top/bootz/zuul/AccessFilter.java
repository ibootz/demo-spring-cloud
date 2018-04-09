package top.bootz.zuul;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class AccessFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();

		log.info("send {} request to {}", req.getMethod(), req.getRequestURL());

		String accessToken = req.getParameter("token");
		if (StringUtils.isBlank(accessToken)) {
			log.warn("token is empty");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			return null;
		}
		log.info("token is ok");
		return null;
	}

}
