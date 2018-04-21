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
import ouhk.comps380f.model.BidRecord;
import ouhk.comps380f.model.Comment;
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
            int price, int bidprice, String body, int status, String bidusername, List<MultipartFile> attachments) throws IOException {
        Item item = new Item();
        item.setCustomerName(customerName);
        item.setSubject(subject);
        item.setBody(body);
        item.setPrice(price);
        item.setBidprice(bidprice);
        item.setStatus(status);
        item.setBidusername(bidusername);
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
    public void updateprice(long id, int price, String bidusername) throws IOException, ItemNotFound {
        Item updatedItem = itemRepo.findOne(id);
        BidRecord bidRecord = new BidRecord();
        if (updatedItem == null) {
            throw new ItemNotFound();
        }
        if (updatedItem.getBidprice() >= price) {
            return;
        }
        bidRecord.setPrice(price);
        bidRecord.setUsername(bidusername);
        bidRecord.setItem(updatedItem);
        updatedItem.setBidprice(price);
        updatedItem.setBidusername(bidusername);
        updatedItem.getBidRecord().add(bidRecord);
        itemRepo.save(updatedItem);
    }

    @Override
    @Transactional(rollbackFor = ItemNotFound.class)
    public void updatestatus(long id, int status) throws IOException, ItemNotFound {
        Item updatedItem = itemRepo.findOne(id);
        if (updatedItem == null) {
            throw new ItemNotFound();
        }
        updatedItem.setStatus(status);
        itemRepo.save(updatedItem);
    }

    @Override
    @Transactional(rollbackFor = ItemNotFound.class)
    public void createComment(long id, String userName, String Comment) throws IOException, ItemNotFound {
        Comment comment = new Comment();
        Item commentItem = itemRepo.findOne(id);
        if (commentItem == null) {
            throw new ItemNotFound();
        }
        comment.setUsername(userName);
        comment.setComment(Comment);
        comment.setItem(commentItem);
        commentItem.getComments().add(comment);
        itemRepo.save(commentItem);
    }

    @Override
    @Transactional
    public void deleteComment(long itemId, long commentId) throws IOException {
        Item deleteCommentItem = itemRepo.findOne(itemId);
        for (Comment comment : deleteCommentItem.getComments()) {
            if (comment.getId() == commentId) {
                deleteCommentItem.deleteComment(comment);
                itemRepo.save(deleteCommentItem);
                return;
            }
        }

    }

}
