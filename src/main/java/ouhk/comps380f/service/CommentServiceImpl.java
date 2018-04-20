/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ouhk.comps380f.dao.CommentRepository;
import ouhk.comps380f.model.Comment;

/**
 *
 * @author Noctis
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepo;

    @Override
    @Transactional
    public Comment getComment(long itemId, String username, String comment) {
        return commentRepo.findByItemId(itemId);
    }
}
