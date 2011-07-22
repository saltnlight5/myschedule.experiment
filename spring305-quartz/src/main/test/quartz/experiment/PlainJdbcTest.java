package quartz.experiment;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PlainJdbcTest {
	@Autowired
	@Qualifier("myTxSchedulerService")
	protected MySchedulerService mySchedulerService;
	
	@Test 
	public void testRemoveJobs() throws Exception {
		mySchedulerService.removeJobs();
	}
	
	//@Test(expected=Exception.class)
	@Test
	public void testScheduleJobsWithExpectedErrors() throws Exception {
		mySchedulerService.showJobs();
		mySchedulerService.scheduleJobs();
	}
	
	@Test 
	public void testShowJobs() throws Exception {
		mySchedulerService.showJobs();
	}
	
	public static class MyService implements MySchedulerService {		
		
		protected Logger logger = LoggerFactory.getLogger(getClass());
		protected JdbcTemplate jdbcTemplate;
		
		public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}
		
		@Override
		public void scheduleJobs() throws Exception {
			int ret = jdbcTemplate.update(
					"insert into qrtz_job_details(job_name, job_group, job_class_name, is_durable, is_volatile, is_stateful, requests_recovery) " +
					"values(?, ?, ?, ?, ?, ?, ?)", 
					"job1", "DEFAULT", SimpleJob.class.getName(), "1", "0", "0", "0");
			logger.info("Record#1 inserted. ret={}", ret);
			
			ret = jdbcTemplate.update(
					"insert into qrtz_job_details(job_name, job_group, job_class_name, is_durable, is_volatile, is_stateful, requests_recovery) " +
					"values(?, ?, ?, ?, ?, ?, ?)", 
					"job1", "DEFAULT", SimpleJob.class.getName(), "1", "0", "0", "0");
			logger.info("Record#2 inserted. ret={}", ret);
		}

		@Override
		public void updateJobs() throws Exception {
		}

		@Override
		public void removeJobs() throws Exception {
			int ret = jdbcTemplate.update("delete from qrtz_job_details");
			logger.info("{} records removed.", ret);
		}

		@Override
		public void showJobs() throws Exception {
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from qrtz_job_details");
			for(Map<String, Object> entry : list){
				logger.info("{}", entry);
			}
			logger.info("{} jobs found.", list.size());
		}		
	}
	
	/** This is not going to work by extending. You must mark @Transactional on the class
	 * that has the tx method.
	@Transactional
	public static class MyTxService extends MyService {
	}
	*/
	
	@Transactional
	public static class MyTxService extends MyService {		
		@Override
		public void scheduleJobs() throws Exception {
			super.scheduleJobs();
		}

		@Override
		public void updateJobs() throws Exception {
			super.updateJobs();
		}

		@Override
		public void removeJobs() throws Exception {
			super.removeJobs();
		}

		@Override
		public void showJobs() throws Exception {
			super.showJobs();
		}		
	}
}
