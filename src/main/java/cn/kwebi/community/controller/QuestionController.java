package cn.kwebi.community.controller;

import cn.kwebi.community.dto.CommentDTO;
import cn.kwebi.community.dto.QuestionDTO;
import cn.kwebi.community.enums.CommentTypeEnum;
import cn.kwebi.community.model.User;
import cn.kwebi.community.service.CommentService;
import cn.kwebi.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           HttpServletRequest request,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);

        List<CommentDTO> comments = commentService.listByParentId(id, CommentTypeEnum.QUESTION);

        questionService.incView(id);//增加阅读数
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);

        User user = (User)request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user",user);
        }else{
            model.addAttribute("user",questionDTO.getUser());
        }
        return "question";
    }
}
