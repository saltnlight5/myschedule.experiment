package quartz.experiment;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Test;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;

/** XmlSchedulingDataProcessorTest
 *
 * @author Zemian Deng
 */
public class XmlSchedulingDataProcessorTest {

	@Test
	public void testXMLSchedulingDataProcessor() throws Exception {
		CascadingClassLoadHelper clhelper = new CascadingClassLoadHelper();
		clhelper.initialize();
		/** We should be getting this log output:
		DEBUG main XMLSchedulingDataProcessor| Utilizing schema packaged in local quartz distribution jar.
		 */
		XMLSchedulingDataProcessor processor = new XMLSchedulingDataProcessor(clhelper);
		assertThat(processor, notNullValue());
	}
	
	@Test
	public void testLocalSchemaLoading() throws Exception {
		String resname = "org/quartz/xml/job_scheduling_data_1_8.xsd";
		
		// Note that if were to use getClass().getResource, the resource name should use '/' prefix
		// or else it will try to resolve by 'modified_package_name'. See Class#resolveName().
		URL url1 = getClass().getResource("/" + resname);
		assertThat(url1, notNullValue());
		
		// Getting the class loader is more normal way to load resource.
		URL url1b = getClass().getClassLoader().getResource(resname);
		assertThat(url1b, notNullValue());
		assertThat(url1, is(url1b));
		
		CascadingClassLoadHelper clhelper = new CascadingClassLoadHelper();
		clhelper.initialize();
		URL url2 = clhelper.getResource(resname);
		assertThat(url1, notNullValue());
		
//		LoadingLoaderClassLoadHelper clhelper = new LoadingLoaderClassLoadHelper();
//		clhelper.initialize();
//		URL url2 = clhelper.getResource(resname);
//		assertThat(url1, notNullValue());
		
		assertThat(url1, is(url2));
	}
}
