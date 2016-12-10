/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import models.Post;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.PostService;
import services.security.CurrentUser;
import services.security.CurrentUserAttached;
import services.security.CustomUserDetails;

/**
 *
 * @author sergio
 */
@Controller("AdminPostController")
@RequestMapping("/admin/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/all")
    public String all(@CurrentUser CustomUserDetails activeUser, Model model){
        List<Post> posts = postService.findPostsByAuthor(activeUser.getId());
        model.addAttribute("posts", posts);
        return "admin/post/all";
    }
    
    @GetMapping("/create")
    public String showCreatePostForm(Model model){
        model.addAttribute("post", new Post());
        return "admin/post/create";
    }
    
    @PostMapping("/create")
    public String processPost(
            @CurrentUserAttached User activeUser,
            @ModelAttribute @Valid Post post, 
            Errors errors){
        if(errors.hasErrors()){
            return "admin/post/create";
        }
        Logger.getLogger(PostController.class.getName()).log(Level.INFO, activeUser.toString());
        post.setAuthor(activeUser);
        postService.create(post);
        return "redirect:/admin/posts/all";
    }
}
