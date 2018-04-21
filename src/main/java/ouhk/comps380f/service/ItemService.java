/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.ItemNotFound;
import ouhk.comps380f.model.Item;

/**
 *
 * @author j
 */
public interface ItemService {

    public long createItem(String customerName, String subject,
            int price, int bidprice, String body, int status, String bidusername, List<MultipartFile> attachments) throws IOException;

    public List<Item> getItems();

    public void updatestatus(long id, int status) throws IOException, ItemNotFound;

    public Item getItem(long id);

    public void createComment(long id, String userName, String comment) throws IOException, ItemNotFound;
    public void deleteComment(long itemId,long commentId) throws IOException;

    public void updateprice(long id, int price, String bidusername) throws IOException, ItemNotFound;

    public void delete(long id) throws ItemNotFound;

    public void deleteAttachment(long itemId, String name)
            throws AttachmentNotFound;
}
