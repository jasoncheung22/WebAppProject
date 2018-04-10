package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Item;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("item")
public class ItemController {

    private volatile long TICKET_ID_SEQUENCE = 1;
    private Map<Long, Item> itemDatabase = new Hashtable<>();

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("itemDatabase", itemDatabase);
        return "list";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("add", "itemForm", new Form());
    }

    public static class Form {

        private String subject;
        private String body;
        private List<MultipartFile> attachments;

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

    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(Form form, Principal principal) throws IOException {
        Item item = new Item();
        item.setId(this.getNextItemId());
        item.setCustomerName(principal.getName());
        item.setSubject(form.getSubject());
        item.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                item.addAttachment(attachment);
            }
        }
        this.itemDatabase.put(item.getId(), item);
        return new RedirectView("/item/view/" + item.getId(), true);
    }

    private synchronized long getNextItemId() {
        return this.TICKET_ID_SEQUENCE++;
    }

    @RequestMapping(value = "view/{itemId}", method = RequestMethod.GET)
    public String view(@PathVariable("itemId") long itemId,
            ModelMap model) {
        Item item = this.itemDatabase.get(itemId);
        if (item == null) {
            return "redirect:/item/list";
        }
        model.addAttribute("itemId", Long.toString(itemId));
        model.addAttribute("item", item);
        return "view";
    }

    @RequestMapping(
            value = "/{itemId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("itemId") long itemId,
            @PathVariable("attachment") String name) {
        Item item = this.itemDatabase.get(itemId);
        if (item != null) {
            Attachment attachment = item.getAttachment(name);
            if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
        }
        return new RedirectView("/item/list", true);
    }

    @RequestMapping(
            value = "/{itemId}/delete/{attachment:.+}",
            method = RequestMethod.GET
    )
    public String deleteAttachment(@PathVariable("itemId") long itemId,
            @PathVariable("attachment") String name) {
        Item item = this.itemDatabase.get(itemId);
        if (item != null) {
            if (item.hasAttachment(name)) {
                item.deleteAttachment(name);
            }
        }
        return "redirect:/item/edit/" + itemId;
    }

    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("itemId") long itemId,
            Principal principal, HttpServletRequest request) {
        Item item = this.itemDatabase.get(itemId);
        if(principal == null){
            return new ModelAndView(new RedirectView("/item/list", true));
        }
        if (item == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(item.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/item/list", true));
        }
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("itemId", Long.toString(itemId));
        modelAndView.addObject("item", item);

        Form itemForm = new Form();
        itemForm.setSubject(item.getSubject());
        itemForm.setBody(item.getBody());
        modelAndView.addObject("itemForm", itemForm);

        return modelAndView;
    }

    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.POST)
    public View edit(@PathVariable("itemId") long itemId, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        Item item = this.itemDatabase.get(itemId);
        if (item == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(item.getCustomerName()))) {
            return new RedirectView("/item/list", true);
        }
        
        item.setSubject(form.getSubject());
        item.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                item.addAttachment(attachment);
            }
        }
        this.itemDatabase.put(item.getId(), item);
        return new RedirectView("/item/view/" + item.getId(), true);
    }

    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItem(@PathVariable("itemId") long itemId) {
        if (this.itemDatabase.containsKey(itemId)) {
            this.itemDatabase.remove(itemId);
        }
        return "redirect:/item/list";
    }

}
