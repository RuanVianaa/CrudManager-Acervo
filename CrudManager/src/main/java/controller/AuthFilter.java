package controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    private static final String[] LIVRES = {
        "/css/", "/js/", "/images/", "/fonts/"
    };

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String caminho = RequestUtil.getAction(req);

        if (ehLivre(caminho)) {
            chain.doFilter(request, response);
            return;
        }

        if (estaLogado(req)) {

            // Impede o armazenamento de páginas privadas no cache
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);

            chain.doFilter(request, response);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @Override
    public void destroy() {
    }

    private boolean ehLivre(String caminho) {
        if (caminho.equals("/login"))
            return true;

        for (String livre : LIVRES) {
            if (caminho.startsWith(livre))
                return true;
        }

        return false;
    }

    private boolean estaLogado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        return session != null
                && session.getAttribute(LoginController.SESSION_USER) != null;
    }
}