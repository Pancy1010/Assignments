package com.work.chinafoodstore.service;

import com.work.chinafoodstore.dommain.Comment;
import com.work.chinafoodstore.mapper.CommentMapper;
import com.work.chinafoodstore.web.dto.CommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Feedback logic processing
 */
@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Save feedback
     *
     * @param commentDto
     * @return
     */
    public int saveComment(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setCreatedTime(new Date());
        return commentMapper.insert(comment);
    }
}
