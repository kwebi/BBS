package cn.kwebi.community.mapper;

import cn.kwebi.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag)" +
            " values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{creator} limit  #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "creator") Integer creator, @Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question where creator=#{creator}")
    Integer countByUserId(@Param(value = "creator") Integer creator);

    //@Select("call selectQuestionById(#{id})")
    @Select("select * from question where id=#{id}")
    Question getById(@Param(value = "id") Integer id);

    @Update("update question set title = #{title}, description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    int update(Question question);

    @Update("update question set view_count = view_count+1 where id=#{id}")
    void incViewCount(@Param(value = "id") Integer id);

    @Update("update question set comment_count = comment_count+1 where id=#{id}")
    void incCommentCount(@Param(value = "id") Integer id);
}
