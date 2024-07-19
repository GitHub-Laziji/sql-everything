package org.laziji.sqleverything.conrtoller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

    @RequestMapping("newProject")
    public String newProject(HttpServletRequest request){
        HttpSession session = request.getSession();
        return "aaa";
    }
}
