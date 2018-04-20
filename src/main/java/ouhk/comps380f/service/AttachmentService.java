/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import ouhk.comps380f.model.Attachment;

/**
 *
 * @author j
 */
public interface AttachmentService {
    public Attachment getAttachment(long ticketId, String name);
}
