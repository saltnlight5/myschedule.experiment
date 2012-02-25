package timemachine;

public class Schedule {
	private Long id;
	private String name;
	private String desc;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Schedule[id=" + id + ", name=" + name + ", desc=" + desc + "]";
	}
}
