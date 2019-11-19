package cn.kwebi.community.service;

import cn.kwebi.community.exception.CustomizeErrorCode;
import cn.kwebi.community.exception.CustomizeException;
import cn.kwebi.community.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    public void insert(Comment comment) {
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARENT_NOT_FOUND);
        }
    }
}
