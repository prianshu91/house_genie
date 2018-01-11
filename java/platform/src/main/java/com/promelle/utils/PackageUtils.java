package com.promelle.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.promelle.exception.AbstractException;

/**
 * This class is intended for providing package related functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class PackageUtils {

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and sub packages.
	 * 
	 * @param packageName
	 * @return The classes
	 * @throws AbstractException
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getClasses(String packageName)
			throws AbstractException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		String path = packageName.replace('.', '/');
		List<File> dirs = new ArrayList<File>();
		try {
			Enumeration<URL> resources = classLoader.getResources(path);
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		List<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and sub
	 * directories.
	 * 
	 * @param directory
	 * @param packageName
	 * @return The classes
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName)
			throws AbstractException {
		List<Class> classes = new ArrayList<Class>();
		if (directory == null || !directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		if (files == null)
			return classes;
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				try {
					classes.add(Class.forName(packageName
							+ '.'
							+ file.getName().substring(0,
									file.getName().length() - 6)));
				} catch (ClassNotFoundException e) {
					throw new AbstractException(e);
				}
			}
		}
		return classes;
	}

	/**
	 * Recursive method used to delete a given directory and sub directories.
	 * 
	 * @param directory
	 * @param packageName
	 * @return The classes
	 * @throws Exception
	 */
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteFolder(file);
				} else {
					file.delete();
				}
			}
		}
		folder.delete();
	}

}
