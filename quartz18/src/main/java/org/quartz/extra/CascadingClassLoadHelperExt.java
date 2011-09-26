package org.quartz.extra;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.quartz.simpl.InitThreadContextClassLoadHelper;
import org.quartz.simpl.LoadingLoaderClassLoadHelper;
import org.quartz.simpl.SimpleClassLoadHelper;
import org.quartz.simpl.ThreadContextClassLoadHelper;
import org.quartz.spi.ClassLoadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is like Quartz's CascadingClassLoadHelper, but for better extensibility. Subclass
 * may initialized their own classLoadHelpers list before initialize() is called.
 * 
 * @author Zemian Deng <saltnlight5@gmail.com>
 * 
 */
public class CascadingClassLoadHelperExt implements ClassLoadHelper {

   /*
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * 
    * Data members.
    * 
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected List<ClassLoadHelper> classLoadHelpers;

    protected ClassLoadHelper bestCandidate;

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
       classLoadHelpers = new ArrayList<ClassLoadHelper>();

       classLoadHelpers.add(new LoadingLoaderClassLoadHelper());
       classLoadHelpers.add(new SimpleClassLoadHelper());
       classLoadHelpers.add(new ThreadContextClassLoadHelper());
       classLoadHelpers.add(new InitThreadContextClassLoadHelper());

       for (ClassLoadHelper loadHelper : classLoadHelpers) {
           logger.debug("Init {}", loadHelper);
           loadHelper.initialize();
       }
   }

   /**
    * Return the class with the given name.
    */
   public Class<?> loadClass(String name) throws ClassNotFoundException {

       if (bestCandidate != null) {
           try {
               logger.debug("loadClass {} using {}", name, bestCandidate);
               return bestCandidate.loadClass(name);
           } catch (Throwable t) {
               bestCandidate = null;
           }
       }

       Throwable throwable = null;
       Class<?> clazz = null;

       for (ClassLoadHelper loadHelper : classLoadHelpers) {
           logger.debug("loadClass {} using {}", name, loadHelper);

           try {
               clazz = loadHelper.loadClass(name);
               bestCandidate = loadHelper;
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
           logger.debug("loadClass {} using {}", name, bestCandidate);
           result = bestCandidate.getResource(name);
           if(result == null)
               bestCandidate = null;
       }

       for (ClassLoadHelper loadHelper : classLoadHelpers) {
           logger.debug("loadClass {} using {}", name, loadHelper);

           result = loadHelper.getResource(name);
           if (result != null) {
               bestCandidate = loadHelper;
               break;
           }
       }

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
           logger.debug("loadClass {} using {}", name, bestCandidate);
           result = bestCandidate.getResourceAsStream(name);
           if(result == null)
               bestCandidate = null;
       }

       for (ClassLoadHelper loadHelper : classLoadHelpers) {
           logger.debug("loadClass {} using {}", name, loadHelper);

           result = loadHelper.getResourceAsStream(name);
           if (result != null) {
               bestCandidate = loadHelper;
               break;
           }
       }

       return result;
   }

   /**
    * Enable sharing of the "best" class-loader with 3rd party.
    *
    * @return the class-loader user be the helper.
    */
   public ClassLoader getClassLoader() {
       ClassLoader ret = (this.bestCandidate == null) ?
               Thread.currentThread().getContextClassLoader() :
               this.bestCandidate.getClassLoader();
       logger.debug("getClassLoader using {}", ret);
       return ret;
   }

}
