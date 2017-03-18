package com.ournews.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2017/1/13 0013.
 */
public class MyUtils {
    public static boolean isNull(String str) {
        return str == null || str.equals("") || str.equals("null");
    }

    public static boolean isLoginName(String str) {
        return str != null && str.length() > 5 && str.length() < 13;
    }

    public static boolean isPassword(String str) {
        return str != null && str.length() > 7 && str.length() < 17;
    }

    public static boolean isToken(String str) {
        return str != null && str.length() == 32;
    }

    public static boolean isVarchar(String str) {
        return !isNull(str) && str.length() <= 50;
    }

    public static boolean isTime(String time) {
        return isNumber(time) && Math.abs(Long.valueOf(time) - System.currentTimeMillis()) <= 2 * 60 * 1000;
    }

    public static boolean isNumber(String str) {
        if (isNull(str))
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber(String str, int startNum, int endNum) {
        return !isNull(str) && isNumber(str) && Integer.valueOf(str) >= startNum && Integer.valueOf(str) <= endNum;
    }

    public static boolean isPhoto(String str, File file) {
        if (str == null || str.equals(""))
            return false;
        if (file != null) {
            File[] list = file.listFiles();
            if (list != null)
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isFile() && list[i].getName().equals(str)) {
                        try {
                            zipImage(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
        }
        return false;
    }

    public static boolean isPhoto(String name) {
        if (isNull(name))
            return false;
        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".bmp"))
            return true;
        return false;
    }

    public static void zipImage(File file) throws IOException {
        if (file.exists()) {
            if (file.length() > 100 * 1024) {
                InputStream is = new FileInputStream(file);
                BufferedImage src = ImageIO.read(file);
                int width = src.getWidth(null);
                int height = src.getHeight(null);
                if (height > width) {
                    width = width * 512 / height;
                    height = 512;
                } else {
                    height = height * 512 / width;
                    width = 512;
                }
                is.close();
                Image image = ImageIO.read(file);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
                FileOutputStream out = new FileOutputStream(file);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();
            }
        }
    }

    public static List<Integer> getNumberList(int allSize) {
        List<Integer> list = new ArrayList<>();

        if (allSize > Constant.RANDOM_NUM) {
            allSize = Constant.RANDOM_NUM;
        }

        while (list.size() < 4) {
            int num = (int) (allSize * Math.random() + 1);
            if (allSize < 4) {
                list.add(num);
            } else {
                if (list.size() == 0) {
                    list.add(num);
                } else {
                    boolean hasSame = false;
                    for (Integer i : list) {
                        if (i == num) {
                            hasSame = true;
                            break;
                        }
                    }
                    if (!hasSame) {
                        list.add(num);
                    }
                }
            }
        }
        return list;
    }
}
