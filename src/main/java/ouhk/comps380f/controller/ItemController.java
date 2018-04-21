package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.ItemNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Item;
import ouhk.comps380f.service.AttachmentService;
import ouhk.comps380f.service.ItemService;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private AttachmentService attachmentService;
// Controller methods, Form object ...

    public static class CommentForm {

        private String username;
        private String comment;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

    }

    public static class Form {

        private String subject;
        private String body;
        private List<MultipartFile> attachments;
        private int price;
        private int bidprice;
        private int status;
        private String bidusername;

        public String getBidusername() {
            return bidusername;
        }

        public void setBidusername(String bidusername) {
            this.bidusername = bidusername;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getBidprice() {
            return bidprice;
        }

        public void setBidprice(int bidprice) {
            this.bidprice = bidprice;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("add", "itemForm", new Form());
    }

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("itemDatabase", itemService.getItems());
        return "list";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(Form form, Principal principal) throws IOException {
        long itemId = itemService.createItem(principal.getName(),
                form.getSubject(), form.getPrice(), form.getBidprice(), form.getBody(), form.getStatus(), form.getBidusername(), form.getAttachments());
        return "redirect:/item/view/" + itemId;
    }

    @RequestMapping(value = "view/{itemId}", method = RequestMethod.GET)
    public String view(@PathVariable("itemId") long itemId,
            ModelMap model) {
        Item item = itemService.getItem(itemId);
        if (item == null) {
            return "redirect:/item/list";
        }
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("item", item);
        return "view";
    }

    @RequestMapping(value = "view/{itemId}/comment", method = RequestMethod.POST)
    public View comment(@PathVariable("itemId") long itemId,
            CommentForm commentForm, Principal principal, HttpServletRequest request)
            throws IOException, ItemNotFound {
        itemService.createComment(itemId, principal.getName(), commentForm.comment);
        return new RedirectView("/item/view/" + itemId, true);
    }

    @RequestMapping(value = "view/{itemId}/comment/delete/{commentId}", method = RequestMethod.GET)
    public View commentDelete(@PathVariable("commentId") long commentId,
            @PathVariable("itemId") long itemId,Principal principal, HttpServletRequest request)
            throws IOException, ItemNotFound {
        itemService.deleteComment(itemId,commentId);
        return new RedirectView("/item/view/" + itemId, true);
    }

    @RequestMapping(value = "view/bid", method = RequestMethod.POST)
    public String bid(@RequestParam("id") long itemId, @RequestParam("bidprice") int price,
            Principal principal, HttpServletRequest request)
            throws IOException, ItemNotFound {
        Item item = itemService.getItem(itemId);
        if (item == null || principal.getName().equals(item.getCustomerName())) {
            return "redirect:/item/list";
        }
        itemService.updateprice(itemId, price, principal.getName());
        return "redirect:/item/view/" + itemId;
    }

    @RequestMapping(value = "view/giveupbid", method = RequestMethod.POST)
    public String giveupbid(@RequestParam("id") long itemId,
            Principal principal, HttpServletRequest request)
            throws IOException, ItemNotFound {
        Item item = itemService.getItem(itemId);
        if (item == null || !principal.getName().equals(item.getCustomerName())) {
            return "redirect:/item/list";
        }
        itemService.updatestatus(itemId, 0);
        return "redirect:/item/view/" + itemId;
    }

    @RequestMapping(value = "view/endbid", method = RequestMethod.POST)
    public String endbid(@RequestParam("id") long itemId,
            Principal principal, HttpServletRequest request)
            throws IOException, ItemNotFound {
        Item item = itemService.getItem(itemId);
        if (item == null || !principal.getName().equals(item.getCustomerName())) {
            return "redirect:/item/list";
        }
        itemService.updatestatus(itemId, 2);
        return "redirect:/item/view/" + itemId;
    }

    @RequestMapping(
            value = "/{itemId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("itemId") long itemId,
            @PathVariable("attachment") String name) {
        Attachment attachment = attachmentService.getAttachment(itemId, name);
        if (attachment != null) {
            return new DownloadingView(attachment.getName(),
                    attachment.getMimeContentType(), attachment.getContents());
        }
        return new RedirectView("/item/list", true);
    }

    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItem(@PathVariable("itemId") long itemId)
            throws ItemNotFound {
        itemService.delete(itemId);
        return "redirect:/item/list";
    }

    @RequestMapping(
            value = "/{itemId}/delete/{attachment:.+}",
            method = RequestMethod.GET
    )
    public String deleteAttachment(@PathVariable("itemId") long itemId,
            @PathVariable("attachment") String name) throws AttachmentNotFound {
        itemService.deleteAttachment(itemId, name);
        return "redirect:/item/edit/" + itemId;
    }
}
