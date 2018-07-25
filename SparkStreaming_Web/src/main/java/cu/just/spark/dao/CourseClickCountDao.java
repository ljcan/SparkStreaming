package cu.just.spark.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cu.just.spark.domain.CourseClickCount;
import cu.just.spark.utils.HBaseUtils;
import groovy.util.logging.Commons;

/**
 * 实战课程访问量DAO层
 * @author shinelon
 *
 */
@Component
public class CourseClickCountDao {
	
	public List<CourseClickCount> query(String dayCourse) throws Exception{
		List<CourseClickCount> list=new ArrayList<CourseClickCount>();
		Map<String,Long> map=new HashMap<String,Long>();
		map=HBaseUtils.getInstance().queryCount("course_clickcount", dayCourse);
		
		for(Map.Entry<String,Long> entry:map.entrySet()) {
			CourseClickCount course=new CourseClickCount();
			course.setName(entry.getKey());
			course.setValue(entry.getValue());
			list.add(course);
		}
		return list;
	}
	
//	public static void main(String[] args) throws Exception{
//		List<CourseClickCount> list=query("20180724");
//		for(CourseClickCount c:list) {
//			System.out.println(c.toString());
//		}
//	}
}
