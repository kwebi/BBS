package cn.kwebi.community.service;

import cn.kwebi.community.dto.CommentDTO;
import cn.kwebi.community.enums.CommentTypeEnum;
import cn.kwebi.community.exception.CustomizeErrorCode;
import cn.kwebi.community.exception.CustomizeException;
import cn.kwebi.community.mapper.CommentMapper;
import cn.kwebi.community.mapper.QuestionMapper;
import cn.kwebi.community.mapper.UserMapper;
import cn.kwebi.community.model.Comment;
import cn.kwebi.community.model.Question;
import cn.kwebi.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARENT_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            //回复评论
            Comment dbComment = commentMapper.getById(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            commentMapper.incCommentCount(dbComment.getId());
        } else {
            Question question = questionMapper.getById(comment.getParentId());
            //回复问题
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            questionMapper.incCommentCount(question.getId());
        }
    }

    public List<CommentDTO> listByParentId(Integer id, CommentTypeEnum commentTypeEnum) {
        List<Comment> comments = commentMapper.getByParentId(id, commentTypeEnum.getType());

        if (comments == null || comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Integer> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        List<User> users = new ArrayList<>();
        for (Integer userId : commentators) {
            users.add(userMapper.findById(userId));
        }
        //加一个map
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        //赋值给commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        Collections.reverse(commentDTOS);
        return commentDTOS;
    }

}
