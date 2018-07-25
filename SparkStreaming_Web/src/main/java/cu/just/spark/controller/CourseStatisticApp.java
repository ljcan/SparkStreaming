package cu.just.spark.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cu.just.spark.dao.CourseClickCountDao;
import cu.just.spark.dao.CourseSearchCountDao;
import cu.just.spark.domain.CourseClickCount;
import net.sf.json.JSONArray;

@RestController
public class CourseStatisticApp {
	//根据课程编号来获取课程名称
	private static Map<String,String> map=new HashMap<String,String>();
	//根据搜索引擎域名来映射搜索引擎名
	private static Map<String,String> searchMap=new HashMap<String,String>();
	static {
		map.put("128", "10小时入门大数据");
		map.put("112", "大数据 Spark SQL慕课网日志分析");
		map.put("145", "深度学习之神经网络核心原理与算法");
		map.put("125", "基于Spring Boot技术栈博客系统企业级前后端实战");
		map.put("130", "Web前端性能优化");
		map.put("131", "引爆潮流技术\r\n" + 
				"Vue+Django REST framework打造生鲜电商项目");
		searchMap.put("cn.bing.com", "微软Bing");
		searchMap.put("www.duba.com", "毒霸网址大全");
		searchMap.put("search.yahoo.com", "雅虎");
		searchMap.put("www.baidu.com", "百度");
		searchMap.put("www.sogou.com", "搜狗");
	}
	
//	@RequestMapping("/get_coursecount")
//	public ModelAndView queryCourse() throws Exception{
//		ModelAndView view=new ModelAndView();
//		List<CourseClickCount> list=CourseClickCountDao.query("20180724");
//		Map<String,Long> model=new HashMap<String, Long>();
//		for(CourseClickCount course:list) {
//			model.put(map.get(course.getName().substring(9)), course.getValue());
//		}
//		JSONArray json=new JSONArray();
//		json.fromObject(model);
//		view.addObject("courseList", json);
//		return view;
//	}
	@Autowired
	CourseClickCountDao courseClickCountDao;
	@Autowired
	CourseSearchCountDao courseSearchCountDao;
	
	@RequestMapping("/get_coursecount")
	@ResponseBody
	public List<CourseClickCount> queryCourse() throws Exception{
		List<CourseClickCount> list=courseClickCountDao.query("20180724");
		for(CourseClickCount course:list) {
			System.out.println(course.toString());
			course.setName(map.get(course.getName().substring(9)));
		}
		return list;
	}
	
	@RequestMapping("/get_courseSearch")
	@ResponseBody
	public List<CourseClickCount> querySearch() throws Exception{
		List<CourseClickCount> list=courseSearchCountDao.query("20180724");
		for(CourseClickCount course:list) {
			System.out.println(course.toString());
			course.setName(searchMap.get(course.getName()));
		}
		return list;
	}
	
	@RequestMapping("/echarts")
	public ModelAndView echarts() {
		return new ModelAndView("course");
	}
	
	@RequestMapping("/search")
	public ModelAndView search() {
		return new ModelAndView("search");
	}

}
