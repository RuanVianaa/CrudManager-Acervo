package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.LoginDAO;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String SESSION_USER = "usuarioLogado";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = RequestUtil.getAction(req);

        if (action.equals("/logout")) {
            logout(req, resp);
            return;
        }

        if (usuarioLogado(req)) {
            ControllerUtil.redirect(resp, req.getContextPath() + "/posts");
            return;
        }

        ControllerUtil.forward(req, resp, "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        if (RequestUtil.isBlank(email) || RequestUtil.isBlank(senha)) {
            erro(req, resp, email, "Informe o e-mail e a senha.");
            return;
        }

        try {
            LoginDAO dao = DAOFactory.createDAO(LoginDAO.class);
            User user = dao.autenticar(email.trim(), senha);

            if (user == null) {
                erro(req, resp, email, "E-mail ou senha inválidos.");
                return;
            }

            req.getSession(true);
            req.changeSessionId();
            req.getSession().setAttribute(SESSION_USER, user);

            ControllerUtil.redirect(resp, req.getContextPath() + "/posts");

        } catch (ModelException e) {
            e.printStackTrace();
            erro(req, resp, email, "Não foi possível entrar agora. Tente novamente.");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        ControllerUtil.redirect(response, request.getContextPath() + "/login");
    }

    private boolean usuarioLogado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute(SESSION_USER) != null;
    }

    private void erro(HttpServletRequest req, HttpServletResponse resp,
            String email, String mensagem)
            throws ServletException, IOException {

        req.setAttribute("erro", mensagem);
        req.setAttribute("email", email);
        ControllerUtil.forward(req, resp, "/login.jsp");
    }
}