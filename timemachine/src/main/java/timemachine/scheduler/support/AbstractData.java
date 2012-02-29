package timemachine.scheduler.support;

import timemachine.scheduler.Job;

public abstract class AbstractData implements Data {
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object other) {
		if (id == null)
			return super.equals(other); // If id is not set, use default
		
		if (!(other instanceof Job))
			return false;

		Job otherJob = (Job)other;
		return id.equals(otherJob.getId());
	}
	
	@Override
	public int hashCode() {
		if (id == null)
			return super.hashCode(); // If id is not set, use default
		
		return id.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + getId() + "]";
	}
}
