/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ouhk.comps380f.dao.AttachmentRepository;
import ouhk.comps380f.dao.ItemRepository;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.ItemNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Item;


/**
 *
 * @author j
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemRepository itemRepo;
    @Resource
    private AttachmentRepository attachmentRepo;

    @Override
    @Transactional
    public List<Item> getItems() {
        return itemRepo.findAll();
    }

    @Override
    @Transactional
    public Item getItem(long id) {
        return itemRepo.findOne(id);
    }

    @Override
    @Transactional(rollbackFor = ItemNotFound.class)
    public void delete(long id) throws ItemNotFound {
        Item deletedItem = itemRepo.findOne(id);
        if (deletedItem == null) {
            throw new ItemNotFound();
        }
        itemRepo.delete(deletedItem);
    }

    @Override
    @Transactional(rollbackFor = AttachmentNotFound.class)
    public void deleteAttachment(long itemId, String name) throws AttachmentNotFound {
        Item item = itemRepo.findOne(itemId);
        for (Attachment attachment : item.getAttachments()) {
            if (attachment.getName().equals(name)) {
                item.deleteAttachment(attachment);
                itemRepo.save(item);
                return;
            }
        }
        throw new AttachmentNotFound();
    }

    @Override
    @Transactional
    public long createItem(String customerName, String subject,
            String body, List<MultipartFile> attachments) throws IOException {
        Item item = new Item();
        item.setCustomerName(customerName);
        item.setSubject(subject);
        item.setBody(body);
        for (MultipartFile filePart : attachments) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            attachment.setItem(item);
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null
                    && attachment.getContents().length > 0) {
                item.getAttachments().add(attachment);
            }
        }
        Item savedItem = itemRepo.save(item);
        return savedItem.getId();
    }

    @Override
    @Transactional(rollbackFor = ItemNotFound.class)
    public void updateItem(long id, String subject,
            String body, List<MultipartFile> attachments)
            throws IOException, ItemNotFound {
        Item updatedItem = itemRepo.findOne(id);
        if (updatedItem == null) {
            throw new ItemNotFound();
        }
        updatedItem.setSubject(subject);
        updatedItem.setBody(body);
        for (MultipartFile filePart : attachments) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            attachment.setItem(updatedItem);
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null
                    && attachment.getContents().length > 0) {
                updatedItem.getAttachments().add(attachment);
            }
        }
        itemRepo.save(updatedItem);
    }
}