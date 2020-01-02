
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Deo-chainmeans on 2017/11/27.
 */

public class FatJar {

    public void createJat() {
        try {
            final File to = new File("aar/temp");
            final File oldTrasenseSupportJar = new File("NeetAnnotationProcessor/build/libs/NeetAnnotationProcessor.jar");
            System.out.println(oldTrasenseSupportJar.getAbsolutePath());
            final File jar = new File("librarys/NeetAnnotation-kapt.jar");
            to.mkdirs();
            System.out.println(to.getAbsolutePath());
            unZip(oldTrasenseSupportJar, to);
            final File libs = new File("NeetAnnotationProcessor/libs/");
            FilenameFilter filenameFilter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            };
            for (File file : libs.listFiles(filenameFilter)) {
                System.out.println(file.getAbsolutePath());
                unZip(file, to);
            }
            zip(to, jar);
            deleteDir(to);
        } catch (Exception e) {
        }
    }

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public static boolean unZip(InputStream from, File to) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(from);
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                final File file = new File(to, zipEntry.getName());
                System.out.println(file.getParentFile() + (file.getParentFile().mkdirs() ? " mkdirs" : " error mkdirs"));
                System.out.println(zipEntry.getName() + (zipEntry.isDirectory() ? " is Directory" : " is not Directory"));
                if (!zipEntry.isDirectory()) {
                    file.createNewFile();
                    if (to.isDirectory()) {
                        final FileOutputStream fileOutputStream = new FileOutputStream(file);
                        try {
                            final byte[] bytes = new byte[1024];
                            int length;
                            while ((length = zipInputStream.read(bytes)) != -1) {
                                fileOutputStream.write(bytes, 0, length);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean unZip(File from, File to) {
        try {
            return unZip(new FileInputStream(from), to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean zip(File from, File to) {
        JarOutputStream zOut = null;
        try {
            to.createNewFile();
            zOut = new JarOutputStream(new FileOutputStream(to));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (zOut == null) {
            return false;
        }
        ArrayList<File> files = new ArrayList<>();
        findFile(files, from);
        for (File file : files) {
            FileInputStream fis = null;
            try {
                final JarEntry zipEntry = new JarEntry(file.getPath().replace("librarys\\temp\\", "").replace("\\","/"));
                zOut.putNextEntry(zipEntry);
                fis = new FileInputStream(file);
                int byteNo;
                byte[] b = new byte[1024];
                while ((byteNo = fis.read(b)) > 0) {
                    zOut.write(b, 0, byteNo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        try {
            zOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void findFile(ArrayList<File> files, File file) {
        if (file.isFile()) {
            files.add(file);
        } else {
            for (File f : file.listFiles()) {
                findFile(files, f);
            }
        }
    }
}
