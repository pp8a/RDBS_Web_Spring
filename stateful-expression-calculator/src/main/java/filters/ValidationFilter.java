package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/calc/*")
public class ValidationFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//checking the URI
		String pathInfo = httpRequest.getPathInfo();
		if(pathInfo == null || !isPathInfo(pathInfo)) {
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			httpResponse.getWriter().write("Invalid URI");
			return;
		}
		
		//checking the method
		String method = httpRequest.getMethod();
		if(!("PUT".equals(method) || "GET".equals(method) || "DELETE".equals(method))) {
			httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);//405
			httpResponse.getWriter().write("Invalid request method");
			return;
		}		
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	private static boolean isPathInfo(String str) {
        return str.matches("/[a-z]+");
    }


}
