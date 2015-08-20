package com.hisihi.controller;

import com.google.gson.Gson;
import com.hisihi.dao.AppUserDao;
import com.hisihi.dao.HiworksDao;
import com.hisihi.service.UserService;
import com.hisihi.utils.UMClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
public class HController {
	private static Logger logger = LoggerFactory.getLogger(HController.class);
    private Gson gson = new Gson();
	@Autowired
	private UserService userService;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private HiworksDao hiworksDao;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(){
		return "login";
	}

    @RequestMapping(value="/hisihi", method=RequestMethod.GET)
    public String hisihi(){
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login(){
        return "login";
    }

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(){
		return "register";
	}

    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logout(){
        return "login";
    }

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String loginPost(Model model, HttpServletRequest request){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (userService.isUserCorrect(username, password)){
            return "index";
		}
		else {
			model.addAttribute("error", "user not exist or password is wrong");
            return "login";
		}
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPost(Model model, HttpServletRequest request){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String adminPassword = request.getParameter("adminPassword");
		if ("027hisihi".equals(adminPassword)){
			if(userService.addUser(username, password)){
				model.addAttribute("name", username);
				return "index";
			} else {
				model.addAttribute("error", "db exception");
				return "register";
			}
		}
		else {
			model.addAttribute("error", "Admin Password Wrong");
			return "register";
		}
	}

	@RequestMapping(value="/app_base_data", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String app_base_data(){
		UMClient client = new UMClient();
		String data = client.getAppsBaseData();
		return this.successResponse(data);
	}

    @RequestMapping(value="/register_total_count", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String register_total_count(){
        int count = appUserDao.getTotalCount();
        Map data = new HashMap();
        data.put("count", count);
        return this.successResponse(data);
    }

    @RequestMapping(value="/hiworks_download_total_count", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String hiworks_download_total_count(){
        int count = hiworksDao.getTotalDownloadCount();
        Map data = new HashMap();
        data.put("count", count);
        return this.successResponse(data);
    }

    @RequestMapping(value="/student_teacher_count", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String student_teacher_count(){
        int studentCount = appUserDao.getStudentsCount();
        int teacherCount = appUserDao.getTeachersCount();
        Map data = new HashMap();
        data.put("teacherCount", teacherCount);
        data.put("studentCount", studentCount);
        return this.successResponse(data);
    }

    private String successResponse(Object data){
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);
        return gson.toJson(map);
    }

    private String errorResponse(int code, String message){
        Map map = new HashMap();
        map.put("success", false);
        map.put("code", code);
        map.put("message",  message);
        return gson.toJson(map);
    }

}
