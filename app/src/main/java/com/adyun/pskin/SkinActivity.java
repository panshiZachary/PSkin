package com.adyun.pskin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.adyun.pskin.skin.Skin;
import com.adyun.pskin.skin.SkinUtils;
import com.adyun.skin.core.SkinManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zachary
 * on 2019/2/11.
 */
public class SkinActivity extends Activity {

    List<Skin> skins = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        skins.add(new Skin("e0893ca73a972d82bcfc3a5a7a83666d", "1111111.skin", "app-skin-debug" +
                ".apk"));

    }

    /**
     * 换肤
     * @param view
     */
    public void change(View view) {
        // 使用第0 个皮肤
        Skin skin = skins.get(0);
        selectSkin(skin);
        SkinManager.getInstance().loadSkin(skin.path);

    }

    /**
     * 还原
     * @param view
     */
    public void restore(View view) {
        SkinManager.getInstance().loadSkin(null);
    }
    private void selectSkin(Skin skin) {
        File theme = new File(getFilesDir(), "theme");
        if (theme.exists() && theme.isFile()){
            theme.delete();
        }
        theme.mkdirs();
        File skinFile = skin.getSkinFile(theme);
        if (skinFile.exists()){
            Log.e("SkinActivity","皮肤已存在，开始换肤");
            return;
        }
        Log.e("SkinActivity","皮肤不存在，开始下载");
        // 临时文件
        File tempSkin = new File(skinFile.getParentFile(), skin.name + ".temp");

        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(tempSkin);
            // 假设下载皮肤包
            is = getAssets().open(skin.url);
            byte[] bytes = new byte[10240];
            int len;
            while ((len =is.read(bytes)) != -1){
                fos.write(bytes,0,len);
            }
            // 下载成功 ，将皮肤包信息insert 已下载数据库
            Log.e("SkinActivity","皮肤包下载完成开始校验");
            if (TextUtils.equals(SkinUtils.getSkinMD5(tempSkin),skin.md5)){
                Log.e("SkinActivity", "校验成功,修改文件名。");
                tempSkin.renameTo(skinFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tempSkin.delete();
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }
}
