package com.hisihi.controller;

import com.google.gson.Gson;
import com.hisihi.dao.AdDao;
import com.hisihi.dao.AppUserDao;
import com.hisihi.dao.ForumDao;
import com.hisihi.dao.HiworksDao;
import com.hisihi.model.ResolvedQuestionBean;
import com.hisihi.service.UserService;
import com.hisihi.utils.HttpClient;
import com.hisihi.utils.HttpResponseWrapper;
import com.hisihi.utils.StringUtils;
import com.hisihi.utils.UMClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private ForumDao forumDao;

    @Autowired
    private AdDao adDao;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String main(){
		return "login";
	}

    @RequestMapping(value="/login")
    public String loginPost(Model model, HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userService.isUserCorrect(username, password)){
            request.getSession().setAttribute("username", username);
            return "redirect:/index";
        }
        else {
            model.addAttribute("error", "user not exist or password is wrong");
            return "login";
        }
    }

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(){
		return "register";
	}

    @RequestMapping(value="/index", method=RequestMethod.GET)
    public String index(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(!StringUtils.isEmpty(name)) {
            return "index";
        } else {
            return "login";
        }
    }

    @RequestMapping(value="/retained", method=RequestMethod.GET)
    public String retained(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(!StringUtils.isEmpty(name)) {
            return "retained";
        } else {
            return "login";
        }
    }

    @RequestMapping(value="/active", method=RequestMethod.GET)
    public String active(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(!StringUtils.isEmpty(name)) {
            return "active";
        } else {
            return "login";
        }
    }

    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("username");
        return "login";
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

    @RequestMapping(value="/hiworks_view_count", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String hiworks_view_count(){
        int count = hiworksDao.getTotalViewCount();
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

    @RequestMapping(value="/questions_resolved_everyday", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String questions_resolved_everyday(){
        List list = forumDao.getEverydayPostCount();
        return this.successResponse(list);
    }

    @RequestMapping(value="/user_active", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String user_active(HttpServletRequest request){
        UMClient client = new UMClient();
        String type = request.getParameter("type");
        String start_date  = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");
        if(StringUtils.isEmpty(start_date))
            start_date = "2015-08-08";
        if(StringUtils.isEmpty(end_date)){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            end_date = df.format(new Date());
        }
        if(StringUtils.isEmpty(type))
            type = "daily";
        List data = client.getActiveUserData(start_date, end_date, type);
        return this.successResponse(data);
    }

    @RequestMapping(value="/user_retained", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String user_retained(HttpServletRequest request){
        UMClient client = new UMClient();
        String type = request.getParameter("type");
        String start_date  = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");
        String device = request.getParameter("device");
        if(StringUtils.isEmpty(start_date))
            start_date = "2015-08-08";
        if(StringUtils.isEmpty(end_date)){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            end_date = df.format(new Date());
        }
        if(StringUtils.isEmpty(type))
            type = "daily";
        if(StringUtils.isEmpty(device))
            device = "Android";
        String data = client.getRetainUserData(start_date, end_date, type, device);
        return this.successResponse(data);
    }

    @RequestMapping(value="/adclick", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String adclick(HttpServletRequest request){

        String appid = request.getParameter("appid");
        String channel  = request.getParameter("channel");
        String mac = request.getParameter("mac");
        String idfa = request.getParameter("idfa");
        String callback = URLDecoder.decode(request.getParameter("callback"));

        int count = adDao.saveAdInfo( appid,  channel,  mac,  idfa,  callback);
        if(count > 0){
            
            Map map = new HashMap();
            map.put("success", true);
            map.put("message", "Success");
            return gson.toJson(map);
        }else{
            Map map = new HashMap();
            map.put("success", false);
            map.put("message", "Error");
            return gson.toJson(map);
        }
    }

    @RequestMapping(value="/registerIDFA", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String registerIDFA(HttpServletRequest request){
        try {
                String idfa = request.getParameter("idfa");
                List list = adDao.getCallbackByIDFA(idfa);
                if(list.size() > 0){
                    Map cmap  = (Map) list.get(0);//取id值最小的
                    String callback = cmap.get("callback").toString().trim();
                    HttpResponseWrapper wrapper =  HttpClient.doGet(callback);
                    String result = wrapper.content;
                    Map rmap = gson.fromJson(result, Map.class);
                    if(rmap.get("success").toString().trim().equals("true")){
                        result = result.replace("\"true\"","true");
                    }else {
                        result = result.replace("\"false\"","false");
                    }
                    return result;
                }else {
                    Map map = new HashMap();
                    map.put("success", false);
                    map.put("message", "this idfa is not exist");
                    return gson.toJson(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Map map = new HashMap();
                map.put("success", false);
                map.put("message", "callback request exception");
                return gson.toJson(map);
            }
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
