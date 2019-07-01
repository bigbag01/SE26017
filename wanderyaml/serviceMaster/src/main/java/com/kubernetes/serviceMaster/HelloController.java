package com.kubernetes.serviceMaster;

import com.kubernetes.serviceMaster.model.Story;
import com.kubernetes.serviceMaster.model.StoryDao;
import com.kubernetes.serviceMaster.model.User;
import com.kubernetes.serviceMaster.model.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by bambihui on 2019/6/26.
 */
@Controller
@ComponentScan(basePackages = "com.kubernetes.serviceMaster")
public class HelloController {

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {
        return "Welcome to Wander SJTU!";
    }

//
//    @RequestMapping(value = "login", method= RequestMethod.GET)
//    public String login(){
//        return "login";
//    }

    @Autowired
    protected UserDao userDao;


    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String login(@RequestBody(required = true) User user) {
        User nuser = userDao.findById(user.getId());
        JSONObject s = new JSONObject();
        if (nuser == null) {
            s.put("msg","failure");
        }
        else{
            s.put("msg","failure");
        }

        return s.toJSONString();
    }

    @Autowired
    protected StoryDao storyDao;

    @RequestMapping(value = "/story", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String story() {
        JSONArray result = new JSONArray();
        List<Story> list = storyDao.findAll();
        for (int i = 0; i < list.size(); i++) {
            Story obj = list.get(i);
            JSONObject s = new JSONObject();
            s.put("name", obj.getName());
            s.put("location",obj.getLocation());
            s.put("story",obj.getStory());
            result.add(s);
        }
        return result.toJSONString();
    }
}

