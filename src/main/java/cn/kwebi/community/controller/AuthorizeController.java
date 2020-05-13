package cn.kwebi.community.controller;

import cn.kwebi.community.dto.AccessTockenDTO;
import cn.kwebi.community.dto.GithubUser;
import cn.kwebi.community.model.User;
import cn.kwebi.community.provider.GithubProvider;
import cn.kwebi.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        String accessTocken;
        AccessTockenDTO accessTockenDTO = new AccessTockenDTO();
        accessTockenDTO.setCode(code);
        accessTockenDTO.setRedirect_uri(redirectUri);
        accessTockenDTO.setState(state);
        accessTockenDTO.setClient_id(clientId);
        accessTockenDTO.setClient_secret(clientSecret);
        accessTocken = githubProvider.getAccessTocken(accessTockenDTO);

        //System.out.println(accessTocken);
        GithubUser githubUser = githubProvider.getUser(accessTocken);
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(githubUser.getId()));
            if (githubUser.getName() == null) {
                user.setName(githubUser.getLogin());
            } else {
                user.setName(githubUser.getName());
            }
            user.setAvatarUrl(githubUser.getAvatarUrl());

            userService.createOrUpdate(user);
            //写cookie
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
