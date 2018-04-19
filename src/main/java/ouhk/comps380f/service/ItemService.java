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
            String body, List<MultipartFile> attachments) throws IOException;

    public List<Item> getItems();

    public Item getItem(long id);

    public void updateItem(long id, String subject,
            String body, List<MultipartFile> attachments)
            throws IOException, ItemNotFound;

    public void delete(long id) throws ItemNotFound;

    public void deleteAttachment(long itemId, String name)
            throws AttachmentNotFound;
}
