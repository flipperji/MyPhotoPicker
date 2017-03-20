package com.flippey.photopicker.entity;

import java.util.ArrayList;

/**
 * @ Author      Flippey
 * @ Creat Time  2017/1/19 21:47
 * 图片文件夹信息
 */

public class ImageFolder {
    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public ImageInformation mImageInformation;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<ImageInformation> images;  //当前文件夹下所有图片的集合

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
