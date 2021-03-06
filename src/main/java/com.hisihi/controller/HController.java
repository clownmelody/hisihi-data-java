package com.hisihi.controller;

import com.google.gson.Gson;
import com.hisihi.dao.*;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


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
    @Autowired
    private AsoDao asoDao;

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
            request.setAttribute("menu", "index");
            return "redirect:/index";
        }
        else {
            model.addAttribute("error", "用户不存在或密码错误");
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
            request.setAttribute("menu", "index");
            request.setAttribute("name", name);
            return "index";
        } else {
            return "login";
        }
    }

    @RequestMapping(value="/retained", method=RequestMethod.GET)
    public String retained(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(!StringUtils.isEmpty(name)) {
            request.setAttribute("menu", "retained");
            request.setAttribute("name", name);
            return "retained";
        } else {
            return "login";
        }
    }

    @RequestMapping(value="/active", method=RequestMethod.GET)
    public String active(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(!StringUtils.isEmpty(name)) {
            request.setAttribute("menu", "active");
            request.setAttribute("name", name);
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

    @RequestMapping(value="/stats", method=RequestMethod.GET)
    public String stats(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(!StringUtils.isEmpty(name)) {
            request.setAttribute("menu", "stats");
            request.setAttribute("name", name);
            return "stats";
        } else {
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

    @RequestMapping(value="/auth/denied", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String auth_denied(){
        Map data = new HashMap();
        data.put("error_msg", "你没有此权限");
        return this.successResponse(data);
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

        int count = adDao.saveAdInfo(appid, channel, mac, idfa, callback);
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
                    int id = Integer.parseInt(cmap.get("id").toString());
                    String callback = cmap.get("callback").toString().trim();
                    HttpResponseWrapper wrapper =  HttpClient.doGet(callback);
                    String result = wrapper.content;
                    Map rmap = gson.fromJson(result, Map.class);
                    if(rmap.get("success").toString().trim().equals("true")){
                        result = result.replace("\"true\"","true");
                        adDao.updateAdStatus(id);//推送成功更新广告状态
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

    @RequestMapping(value="/recordChannel", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String recordChannel(HttpServletRequest request){
        try {
            String channel = request.getParameter("channel");
            String iemi = request.getParameter("imei");
            if(channel.contains("school")){
                channel = channel.substring(6, channel.length());
            }
            logger.info("app 渠道上报 -- channel: "+channel+" -- imei: "+iemi);
            if(!adDao.isIEMIExist(iemi)) {
                int count = adDao.recordChannel(channel, iemi);
                if (count > 0) {
                    Map map = new HashMap();
                    map.put("success", true);
                    map.put("message", "Success");
                    logger.info("app 渠道上报 -- channel: " + channel + " -- imei: " + iemi + " --渠道上报成功");
                    return gson.toJson(map);
                } else {
                    Map map = new HashMap();
                    map.put("success", false);
                    map.put("message", "record failed");
                    return gson.toJson(map);
                }
            }else {
                Map map = new HashMap();
                map.put("success", true);
                map.put("message", "Success");
                logger.info("app 渠道上报 -- channel: " + channel + " -- imei: " + iemi + " --已经上报并记录过了");
                return gson.toJson(map);
            }
        } catch (Exception e) {
            logger.info("app 渠道上报异常: " + e.getMessage());
            Map map = new HashMap();
            map.put("success", false);
            map.put("message", "callback request exception");
            return gson.toJson(map);
        }
    }

    @RequestMapping(value="/queryChannelCount", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String queryChannelCount(HttpServletRequest request){
        String channel = request.getParameter("channel");
        if(StringUtils.isEmpty(channel)){
            Map map = new HashMap();
            map.put("message", "渠道不能为空");
            return gson.toJson(map);
        }
        int count = userService.getChannelCount(channel);
        Map map = new HashMap();
        map.put("success", true);
        map.put("message", "Success");
        map.put("count", count);
        return gson.toJson(map);
    }


    @RequestMapping(value="/user_post_count", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String user_post_count(HttpServletRequest request){
        String user_mobile = request.getParameter("user_mobile");
        int user_count = Integer.parseInt(request.getParameter("user_count"));
        String start_date = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");
        if(StringUtils.isEmpty(user_mobile)||user_count==0||
                StringUtils.isEmpty(start_date)||StringUtils.isEmpty(end_date)){
            Map map = new HashMap();
            map.put("message", "参数不能为空");
            return gson.toJson(map);
        }
        List<Map> dataList = new ArrayList<Map>();
        for(int i=0; i<user_count; i++){
            long mobile_num = Long.parseLong(user_mobile);
            mobile_num = mobile_num + i;
            String mobile= String.valueOf(mobile_num);
            int postCount = userService.getPostCount(mobile, start_date, end_date);
            int postReplyCount = userService.getPostReplyCount(mobile, start_date, end_date);
            Map obj = new HashMap();
            obj.put("mobile", mobile);
            obj.put("postCount", postCount);
            obj.put("postReplyCount", postReplyCount);
            dataList.add(obj);
        }
        Map map = new HashMap();
        map.put("success", true);
        map.put("message", "Success");
        map.put("data", dataList);
        return gson.toJson(map);
    }

    @RequestMapping(value="/queryAllChannelCount", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String queryAllChannelCount(HttpServletRequest request){
        int count = userService.getAllChannelCount();
        Map map = new HashMap();
        map.put("success", true);
        map.put("message", "Success");
        map.put("count", count);
        return gson.toJson(map);
    }

    @RequestMapping(value="/aso_click", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String aso_click(HttpServletRequest request){
        String appid = request.getParameter("appid");
        String source  = request.getParameter("source");
        String mac = request.getParameter("mac");
        String idfa = request.getParameter("idfa");
        String ip = request.getParameter("ip");
        String callback = URLDecoder.decode(request.getParameter("callback"));

        int count = asoDao.saveAsoInfo(appid, ip, mac, idfa, callback, source);
        if(count > 0){
            Map map = new HashMap();
            map.put("success", true);
            map.put("message", "ok");
            map.put("code",200);
            return gson.toJson(map);
        }else{
            return this.errorResponse(400,"false");
        }
    }

    @RequestMapping(value="/is_idfa_download", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String is_idfa_download(HttpServletRequest request){
        String appid = request.getParameter("appid");
        String idfa = request.getParameter("idfa");

        Map map = asoDao.isIDFADownload(appid, idfa);
        return gson.toJson(map);
    }

    @RequestMapping(value="/register_aso_client", method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String register_aso_client(HttpServletRequest request){
        try {
            String idfa = request.getParameter("idfa");
            List list = asoDao.getCallbackByIDFA(idfa);
            if(list.size() > 0){
                Map cmap  = (Map) list.get(0);//取id值最小的
                int id = Integer.parseInt(cmap.get("id").toString());
                String callback = cmap.get("callback").toString().trim();
                HttpResponseWrapper wrapper =  HttpClient.doGet(callback);
                String result = wrapper.content;
                Map rmap = gson.fromJson(result, Map.class);
                if(rmap.get("message").toString().trim().equals("ok")){
                    asoDao.updateAsoStatus(id);//推送成功更新aso状态
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
