/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ouhk.comps380f.dao.AttachmentRepository;
import ouhk.comps380f.model.Attachment;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Resource
    private AttachmentRepository attachmentRepo;

    @Override
    @Transactional
    public Attachment getAttachment(long itemId, String name) {
        return attachmentRepo.findByItemIdAndName(itemId, name);
    }
}
