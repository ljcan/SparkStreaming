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
public class CourseSearchCountDao {
	
	/**
	 * 从hbase获取的数据中的rowkey中截取域名返回
	 * @param dayCourse
	 * @return
	 * @throws Exception
	 */
	public Map<String,Long> queryMap(String dayCourse) throws Exception{
		Map<String,Long> map=new HashMap<String,Long>();
		map=HBaseUtils.getInstance().queryCount("course_search_clickcount", dayCourse);
		Map<String,Long> totalMap=new HashMap<String,Long>();
		for(Map.Entry<String,Long> entry:map.entrySet()) {
			int index=entry.getKey().lastIndexOf("_");
			String name=entry.getKey().substring(9,index);
			Long value=entry.getValue();
			if(totalMap.entrySet()!=null) {
				if(totalMap.containsKey(name)) {
					Long v=totalMap.get(name)+value;
					totalMap.put(name, v);
				}else {
					totalMap.put(name, value);
				}
			}else {
				totalMap.put(name, value);
			}
		}
		return totalMap;
	}
	
	public List<CourseClickCount> query(String dayCourse) throws Exception{
		List<CourseClickCount> list=new ArrayList<CourseClickCount>();
		Map<String,Long> map=new HashMap<String,Long>();
		map=queryMap(dayCourse);
		for(Map.Entry<String,Long> m:map.entrySet()) {
			CourseClickCount course=new CourseClickCount();
			course.setName(m.getKey());
			course.setValue(m.getValue());
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
