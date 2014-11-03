package com.bstek.designer.core.actions;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-12
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
public class UpdateConfigIconsProcessor {

    private static final String TITLE = "Update Config Icon";

    private Project project;

    private File rootFolder;

    private String[] classPathes;


    public UpdateConfigIconsProcessor(File rootFolder, String[] classPathes) {
        this.rootFolder = rootFolder;
        this.classPathes = classPathes;
    }

    public void run() {
        process();
    }


    public void process() {

        final Runnable process = new Runnable() {
            public void run() {
                final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
                try {
                    if (indicator != null) {
                        indicator.setIndeterminate(false);
                        indicator.setFraction(0.10);
                        indicator.setText("Updating dorado7 config icons...");
                        createImageRegistry(classPathes, rootFolder);
                        indicator.setFraction(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                ProgressManager.getInstance().runProcessWithProgressSynchronously(process
                        , TITLE, true, project);
            }
        });
    }

    public void createImageRegistry(String[] classpathes, File rootFolder) {
        byte[] buf = new byte[1024];
        File dir = rootFolder;
        String fileName = null;
        String dirName = null;
        try {
            for (String classpath : classpathes) {
                File classesFile = new File(classpath);
                if (!classesFile.isDirectory()) {
                    // --守护包含dorado字符的jar文件
                    if (StringUtils.contains(classesFile.getName(), "dorado")) {
                        ZipFile zipfile = new ZipFile(classesFile);
                        Enumeration zipEntries = zipfile.entries();
                        while (zipEntries.hasMoreElements()) {
                            ZipEntry entry = (ZipEntry) zipEntries
                                    .nextElement();
                            String entryName = entry.getName();
                            int index = entryName.lastIndexOf('/');
                            if (entry.isDirectory()) {
                                dirName = entryName.substring(0, index);
                                dir = new File(rootFolder.getAbsolutePath() + File.separator + dirName);
                                dir.mkdirs();
                                continue;
                            }
                            if (index > 0)
                                dir = new File(rootFolder.getAbsolutePath() + File.separator + entryName.substring(0, index));
                            fileName = entryName.substring(index + 1);
                            if (fileName.endsWith("png")
                                    || fileName.endsWith("gif")) {
                                File imageFile = new File(dir, fileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(
                                        imageFile);
                                // --获取原始图片流信息
                                BufferedInputStream zipInputStream = new BufferedInputStream(
                                        zipfile.getInputStream(entry));
                                int count;
                                while ((count = zipInputStream.read(buf, 0,
                                        1024)) != -1)
                                    fileOutputStream.write(buf, 0, count);
                                fileOutputStream.close();
                                fileOutputStream.flush();
                                zipInputStream.close();
                                //createImageRegistry(entryName, imageFile);
                            }
                        }
                    }
                }
//                else {
//                    File[] srcFiles = classesFile.listFiles();
//                    for (int i = 0; i < srcFiles.length; i++) {
//                        String srcFileName = srcFiles[i].getName();
//                        if (StringUtils.equals(".svn", srcFileName))
//                            continue;
//                        if (srcFiles[i].isDirectory()) {
//                            // --注册目录
//                            dir = root.append(srcFileName).toFile();
//                            if (!dir.exists()) {
//                                dir.mkdirs();
//                            }
//                            createImageRegistry(dir, srcFiles[i]);
//                        } else if (srcFiles[i].isFile()) {
//                            FileOutputStream fileOutputStream;
//                            if (srcFileName.endsWith("png")) {
//                                File imageFile = new File(dir, srcFileName);
//                                String pathName = StringUtils.substringAfter(
//                                        imageFile.getAbsolutePath(),
//                                        ".settings");
//                                fileOutputStream = new FileOutputStream(
//                                        imageFile);
//                                // --获取原始图片流信息
//                                BufferedInputStream bufferedInputStream = new BufferedInputStream(
//                                        new FileInputStream(srcFiles[i]));
//                                int count;
//                                while ((count = bufferedInputStream.read(buf,
//                                        0, 1024)) != -1)
//                                    fileOutputStream.write(buf, 0, count);
//                                fileOutputStream.close();
//                                fileOutputStream.flush();
//                                bufferedInputStream.close();
//                                createImageRegistry(pathName, imageFile);
//                            }
//                        }
//                    }
//                }
            }
            cleanBlankFolder(rootFolder);
            File doradoWebIconFolder = new File(rootFolder + File.separator + "dorado");
            if (doradoWebIconFolder.exists()) {
                FileUtil.delete(doradoWebIconFolder);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cleanBlankFolder(File srcFile) {
        if (srcFile.isDirectory()) {
            if (srcFile.listFiles().length > 0) {
                for (File file : srcFile.listFiles()) {
                    if (file.isDirectory()) {
                        cleanBlankFolder(file);
                    }
                }
            } else {
                srcFile.delete();
                cleanBlankFolder(srcFile.getParentFile());
            }
        }
    }


}
