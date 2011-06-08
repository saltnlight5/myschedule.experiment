package quartz.experiment;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.quartz.CronExpression;

/** CronTest
 *
 * @author Zemian Deng
 */
public class CronTest {

	@Test
	public void testW_Character() throws Exception {
		assertThat(CronExpression.isValidExpression("0 0 12 1W * ?"), is(true));
		assertThat(CronExpression.isValidExpression("0 0 12 W * ?"), is(true));
	}
}
