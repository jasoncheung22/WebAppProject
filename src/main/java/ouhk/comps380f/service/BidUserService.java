/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouhk.comps380f.dao.BidUserRepository;
import ouhk.comps380f.exception.ItemNotFound;
import ouhk.comps380f.model.BidUser;
import ouhk.comps380f.model.UserRole;

/**
 *
 * @author j
 */
@Service
public class BidUserService implements UserDetailsService {

    @Resource
    BidUserRepository bidUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        BidUser bidUser = bidUserRepo.findOne(username);
        if (bidUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : bidUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new User(bidUser.getUsername(), bidUser.getPassword(), authorities);
    }

    @Transactional(rollbackFor = ItemNotFound.class)
    public void updateUser(String Username,String Password, List<UserRole> role) throws ItemNotFound {
        BidUser updatedbidUser = bidUserRepo.findOne(Username);
        if (updatedbidUser == null) {
            throw new ItemNotFound();
        }
        updatedbidUser.setUsername(Username);
        updatedbidUser.setPassword(Password); 
        updatedbidUser.setRoles(role);
        bidUserRepo.save(updatedbidUser);
    }
}
