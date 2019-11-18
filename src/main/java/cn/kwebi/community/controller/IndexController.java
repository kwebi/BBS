package cn.kwebi.community.controller;

import cn.kwebi.community.dto.PaginationDTO;
import cn.kwebi.community.dto.QuestionDTO;
import cn.kwebi.community.mapper.QuestionMapper;
import cn.kwebi.community.mapper.UserMapper;
import cn.kwebi.community.model.Question;
import cn.kwebi.community.model.User;
import cn.kwebi.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "2") Integer size) {


        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
