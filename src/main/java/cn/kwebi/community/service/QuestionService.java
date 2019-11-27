package cn.kwebi.community.service;

import cn.kwebi.community.dto.PaginationDTO;
import cn.kwebi.community.dto.QuestionDTO;
import cn.kwebi.community.exception.CustomizeErrorCode;
import cn.kwebi.community.exception.CustomizeException;
import cn.kwebi.community.mapper.QuestionMapper;
import cn.kwebi.community.mapper.UserMapper;
import cn.kwebi.community.model.Question;
import cn.kwebi.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();
        PaginationDTO paginationDTO = new PaginationDTO();

        if (page < 1) page = 1;
        Integer totalPage = (totalCount + size - 1) / size;
        if (page > totalPage) page = totalPage;
        Integer offset = size*(page-1);
        if(offset<0)offset=0;
        paginationDTO.setPagination(totalPage,page);
        List<Question> list = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question: list){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Integer id, Integer page, Integer size) {
        Integer totalCount = questionMapper.countByUserId(id);
        PaginationDTO paginationDTO = new PaginationDTO();

        if (page < 1) page = 1;
        Integer totalPage = (totalCount + size - 1) / size;
        if (page > totalPage) page = totalPage;
        Integer offset = size*(page-1);
        if(offset<0)offset=0;
        paginationDTO.setPagination(totalPage,page);
        List<Question> list = questionMapper.listByUserId(id,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question: list){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }


    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.create(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.update(question);
            if(update==0){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        questionMapper.incViewCount(id);
    }
}
