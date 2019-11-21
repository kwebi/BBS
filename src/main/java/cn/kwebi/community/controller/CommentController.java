package cn.kwebi.community.controller;

import cn.kwebi.community.dto.CommentCreateDTO;
import cn.kwebi.community.dto.CommentDTO;
import cn.kwebi.community.dto.ResultDTO;
import cn.kwebi.community.enums.CommentTypeEnum;
import cn.kwebi.community.exception.CustomizeErrorCode;
import cn.kwebi.community.model.Comment;
import cn.kwebi.community.model.User;
import cn.kwebi.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setCommentator(user.getId());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.OkOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id){
        List<CommentDTO> commentDTOS = commentService.listByParentId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.OkOf(commentDTOS);
    }
}
