package tests.integration.misc;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tests.unit.misc.Quick3Test;
import tests.unit.misc.QuickTest;

/**
 * If you are to run this Test, you should adjust your pom.xml to only run by suite, else
 * you will run double tests. If you just want a way to separate IntegrationTest, you should try
 * the maven-failsafe-plugin.
 * 
 * @author Zemian Deng
 */
@RunWith(Suite.class)
@SuiteClasses({
	QuickTest.class,
	Quick3Test.class})
public class IntegrationSuiteTest {

	protected static Logger logger = LoggerFactory.getLogger(IntegrationSuiteTest.class);

	@BeforeClass
	public static void suiteSetup() {
		logger.info("Suite before class setup.");
	}
}
