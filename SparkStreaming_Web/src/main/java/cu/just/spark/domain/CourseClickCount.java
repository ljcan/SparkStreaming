package cu.just.spark.domain;
/**
 * 前端echarts web展示的实体类
 * @author shinelon
 *
 */
public class CourseClickCount {
	private String name;
	private Long value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CourseClickCount [name=" + name + ", value=" + value + "]";
	}
	

}
