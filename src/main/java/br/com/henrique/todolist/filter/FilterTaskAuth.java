package br.com.henrique.todolist.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.henrique.todolist.user.UserModel;
import br.com.henrique.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FilterTaskAuth extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        System.out.println("Primeiro console");
        if (servletPath.startsWith("/tasks")) {
            System.out.println("segundo console");
            // pegar autenticacao (user e senha)
            String authorizationHeader = request.getHeader("Authorization");

            String user_password = authorizationHeader.substring("Basic".length()).trim();

            byte[] authDecoder = Base64.getDecoder().decode(user_password);

            String authString = new String(authDecoder);

            String[] credentials = authString.split(":");
            String userName = credentials[0];
            String password = credentials[1];

            // validar o user
            Optional<UserModel> userExists = this.userRepository.findByUsername(userName);
            if (!userExists.isPresent()) {
                response.sendError(401);
            } else {
                // validar a senha
                BCrypt.Result result = BCrypt
                        .verifyer()
                        .verify(password.toCharArray(), userExists.get().getPassword());

                if (result.verified) {
                    request.setAttribute("idUser", userExists.get().getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            if (servletPath.startsWith("/user/save")) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(401);

            }
        }

    }

}
