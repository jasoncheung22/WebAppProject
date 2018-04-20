/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.dao.BidUserRepository;
import ouhk.comps380f.exception.ItemNotFound;
import ouhk.comps380f.model.BidUser;
import ouhk.comps380f.model.UserRole;
import ouhk.comps380f.service.BidUserService;

/**
 *
 * @author j
 */
@Controller
@RequestMapping("user")
public class BidUserController {

    @Resource
    BidUserRepository bidUserRepo;
    @Autowired
    private BidUserService UserService;

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("bidUsers", bidUserRepo.findAll());
        return "listUser";
    }

    public static class Form {

        private String username;
        private String password;
        private String[] roles;
// ... getters and setters for each of the properties

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }

    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("addUser", "bidUser", new Form());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(Form form) throws IOException {
        BidUser user = new BidUser(form.getUsername(),
                form.getPassword(), form.getRoles()
        );
        bidUserRepo.save(user);
        return new RedirectView("/item/list", true);
    }

    @RequestMapping(value = "delete/{username}", method = RequestMethod.GET)
    public View deleteBid(@PathVariable("username") String username, HttpServletRequest request) {
        if (!request.isUserInRole("ROLE_ADMIN")) {
            return new RedirectView("/user/list", true);
        }
        bidUserRepo.delete(bidUserRepo.findOne(username));
        return new RedirectView("/user/list", true);
    }

    @RequestMapping(value = "edit/{username}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("username") String username, Principal principal, HttpServletRequest request) {
        BidUser User = bidUserRepo.findOne(username);
        if (!request.isUserInRole("ROLE_ADMIN")) {
            return new ModelAndView(new RedirectView("/item/list", true));
        }
        if (User.getUsername().equals(principal.getName())) {
            return new ModelAndView(new RedirectView("/user", true));
        }
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("User", User);
        Form UserForm = new Form();
        UserForm.setUsername(User.getUsername());
        UserForm.setPassword(User.getPassword());
        List<String> roles = new ArrayList<>();
        for (UserRole role : User.getRoles()) {
            roles.add(role.getRole());
        }
        UserForm.setRoles(roles.toArray(new String[roles.size()]));
        modelAndView.addObject("UserForm", UserForm);
        return modelAndView;
    }

    @RequestMapping(value = "edit/{username}", method = RequestMethod.POST)
    public View edit(@PathVariable("username") String username, Form form,
            Principal principal, HttpServletRequest request) throws ItemNotFound {
        BidUser User = bidUserRepo.findOne(username);
        List<UserRole> list = new ArrayList<>();
        if (!request.isUserInRole("ROLE_ADMIN")) {
            return new RedirectView("/item/list", true);
        }
        if (User.getUsername().equals(principal.getName())) {
            return new RedirectView("/user", true);
        }
        for (String role : form.getRoles()) {
            UserRole role1 = new UserRole(User, role);
            list.add(role1);
        }

        UserService.updateUser(form.getUsername(), form.getPassword(), list);
        return new RedirectView("/item/list", true);
    }

}
