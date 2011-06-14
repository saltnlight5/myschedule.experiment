package quartz.experiment.spring;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.quartz.simpl.InitThreadContextClassLoadHelper;
import org.quartz.simpl.LoadingLoaderClassLoadHelper;
import org.quartz.simpl.SimpleClassLoadHelper;
import org.quartz.simpl.ThreadContextClassLoadHelper;
import org.quartz.spi.ClassLoadHelper;

/** 
 * Just like org.quartz.simpl.CascadingClassLoadHelper but more open and support adding additional
 * ClassLoadHelper. Also clean up some of the missing generic warnings.
 * 
 * <p>
 * This is needed in order that Quartz can load job_scheduling_data.xsd schema from the quartz jar
 * and not default to public web url, which will not work if you are offline!
 * 
 * <p>
 * TODO: this class has lot's of repetitive code! should clean it up.
 * 
 * @author Zemian Deng
 */
public class CascadingClassLoadHelperExt implements ClassLoadHelper {
	/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected List<ClassLoadHelper> loadHelpers = new LinkedList<ClassLoadHelper>();

    protected ClassLoadHelper bestCandidate;
    
	public void addClassLoadHelper(ClassLoadHelper clhelper) {
		loadHelpers.add(clhelper);
	}
	
	public void addDefaultClassLoadHelpers() {
    	loadHelpers.add(new LoadingLoaderClassLoadHelper());
        loadHelpers.add(new SimpleClassLoadHelper());
        loadHelpers.add(new ThreadContextClassLoadHelper());
        loadHelpers.add(new InitThreadContextClassLoadHelper());
	}

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Called to give the ClassLoadHelper a chance to initialize itself,
     * including the opportunity to "steal" the class loader off of the calling
     * thread, which is the thread that is initializing Quartz.
     */
    public void initialize() {
    	if (loadHelpers.size() == 0) {
    		addDefaultClassLoadHelpers();
    	}
    	
        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            ClassLoadHelper loadHelper = (ClassLoadHelper) iter.next();
            loadHelper.initialize();
        }
    }

    /**
     * Return the class with the given name.
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        if (bestCandidate != null) {
            try {
                return bestCandidate.loadClass(name);
            } catch (Throwable t) {
                bestCandidate = null;
            }
        }

        Throwable throwable = null;
        Class<?> clazz = null;
        ClassLoadHelper loadHelper = null;

        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = (ClassLoadHelper) iter.next();

            try {
                clazz = loadHelper.loadClass(name);
                break;
            } catch (Throwable t) {
                throwable = t;
            }
        }

        if (clazz == null) {
            if (throwable instanceof ClassNotFoundException) {
                throw (ClassNotFoundException)throwable;
            } 
            else {
                throw new ClassNotFoundException( String.format( "Unable to load class %s by any known loaders.", name), throwable);
            } 
        }

        bestCandidate = loadHelper;

        return clazz;
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * @param name name of the desired resource
     * @return a java.net.URL object
     */
    public URL getResource(String name) {

        URL result = null;

        if (bestCandidate != null) {
            result = bestCandidate.getResource(name);
            if(result == null)
                bestCandidate = null;
        }

        ClassLoadHelper loadHelper = null;

        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = (ClassLoadHelper) iter.next();

            result = loadHelper.getResource(name);
            if (result != null) {
                break;
            }
        }

        bestCandidate = loadHelper;
        return result;
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * @param name name of the desired resource
     * @return a java.io.InputStream object
     */
    public InputStream getResourceAsStream(String name) {

        InputStream result = null;

        if (bestCandidate != null) {
            result = bestCandidate.getResourceAsStream(name);
            if(result == null)
                bestCandidate = null;
        }

        ClassLoadHelper loadHelper = null;

        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = (ClassLoadHelper) iter.next();

            result = loadHelper.getResourceAsStream(name);
            if (result != null) {
                break;
            }
        }

        bestCandidate = loadHelper;
        return result;
    }

    /**
     * Enable sharing of the "best" class-loader with 3rd party.
     *
     * @return the class-loader user be the helper.
     */
    public ClassLoader getClassLoader() {
        return (this.bestCandidate == null) ?
                Thread.currentThread().getContextClassLoader() :
                this.bestCandidate.getClassLoader();
    }
}
