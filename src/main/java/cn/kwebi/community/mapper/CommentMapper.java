package cn.kwebi.community.mapper;

import cn.kwebi.community.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("select * from comment where parent_id=#{parentId} and type=#{type}")
    List<Comment> getByParentId(@Param(value = "parentId") Integer parentId, @Param(value = "type") Integer type);

    @Insert("insert into comment (parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id=#{id}")
    Comment getById(@Param(value = "id") Integer id);

    @Update("update comment set comment_count = comment_count+1 where id=#{id}")
    void incCommentCount(@Param(value = "id") Integer id);
}
