/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot.plugin;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

class JarFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith(".jar"));
    }
}

public class PluginFinder {

    // Parameters
    private static final Class<?>[] parameters = new Class<?>[]{URL.class};
    private List<SkypeBotPlugin> pluginCollection;

    public PluginFinder() {
        pluginCollection = new ArrayList<SkypeBotPlugin>(5);
    }

    public List<SkypeBotPlugin> search(String directory) {
        try {
            File dir = new File(directory);
            if (dir.isFile()) {
                return null;
            }

            LinkedList<File> files = new LinkedList<File>();
            new File(directory + "/PluginLoadOrder.txt").createNewFile();
            BufferedReader in = new BufferedReader(new FileReader(directory + "/PluginLoadOrder.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                files.add(new File(directory + "/" + str));
            }
            in.close();

            for (File fil : dir.listFiles(new JarFilter())) {
                if (!files.contains(fil)) {
                    files.add(fil);
                }
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    search(f.getAbsolutePath());
                }
                List<String> classNames = getClassNames(f.getAbsolutePath());
                for (String className : classNames) {
                    // Remove the “.class” at the back
                    String name = className.substring(0, className.length() - 6);
                    Class clazz = getClass(f, name);
                    Class[] interfaces = clazz.getInterfaces();
                    for (Class c : interfaces) {
                        // Implement the IPlugin interface
                        if (c.getName().equals("com.skype.bot.plugin.SkypeBotPlugin")) {
                            pluginCollection.add((SkypeBotPlugin) clazz.newInstance());
                        }
                    }
                }
            }

        } catch (Exception ex) {
        }
        return pluginCollection;
    }

    protected List<String> getClassNames(String jarName) throws IOException {
        ArrayList<String> classes = new ArrayList<String>(10);
        JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
        JarEntry jarEntry;
        while (true) {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            if (jarEntry.getName().endsWith(".class")) {
                classes.add(jarEntry.getName().replaceAll("/", "\\."));
            }
        }

        return classes;
    }

    public Class getClass(File file, String name) throws Exception {
        addURL(file.toURI().toURL());

        URLClassLoader clazzLoader;
        Class clazz;
        String filePath = file.getAbsolutePath();
        filePath = "jar:file://" + filePath + "!/";
        URL url = new File(filePath).toURI().toURL();
        clazzLoader = new URLClassLoader(new URL[]{url});
        //System.out.println(name);
        clazz = clazzLoader.loadClass(name);
        return clazz;

    }

    public void addURL(URL u) throws IOException {
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urls[] = sysLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            if (urls[i].toString().equalsIgnoreCase(u.toString())) {
                return;
            }
        }
        Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysLoader, new Object[]{u});
        } catch (Throwable t) {
            //t.printStackTrace();
            throw new IOException(
                    "Error , could not add URL to system classloader");
        }
    }

    public List<SkypeBotPlugin> getPluginCollection() {
        return pluginCollection;
    }

    public void setPluginCollection(List<SkypeBotPlugin> pluginCollection) {
        this.pluginCollection = pluginCollection;
    }
}
