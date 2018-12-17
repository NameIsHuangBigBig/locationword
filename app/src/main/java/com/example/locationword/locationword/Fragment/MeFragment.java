package com.example.locationword.locationword.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.locationword.locationword.ModifyAgeActivity;
import com.example.locationword.locationword.ModifyNameActivity;
import com.example.locationword.locationword.ModifyPhoneActivity;
import com.example.locationword.locationword.ModifyRealnameActivity;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.SetActivity;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.easeui.API;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment implements View.OnClickListener {
    private ImageView imgHead = null;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvRealname;
    private TextView tvSex;
    private LinearLayout shez;
    private TextView camera;
    private  TextView pic;
    private  TextView cancel;
    private  Dialog iconDialog_mine;
    private android.support.v7.app.AlertDialog al;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
    private static final int CODE_RESULT_REQUEST = 0xa2;//最终裁剪后的结果



    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 80;
    private static int output_Y = 80;


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    Log.i("testttt",result);
                    final JsonObject jo =JSONChange.StringToJsonObject(result);
                    analysis(jo);
                    break;
                case 1001:
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(getContext(),"网络异常！");
                        }
                    });
                    break;
                case 500:
                    //昵称
                    String result1 = (String)msg.obj;
                    Log.i("testttt1",result1);
                case 600:
                    //真实姓名
                    String result2 = (String)msg.obj;
                    Log.i("testttt22",result2);
                    break;
                case 700:
                    //性别
                    String result3 = (String)msg.obj;
                    Log.i("testttt3",result3);
                    break;
                case 800:
                    //电话
                    String result4 = (String)msg.obj;
                    Log.i("testttt3",result4);
                    break;
                case 900:
                    //电话
                    String result5 = (String)msg.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("ddd","清除glide缓存");
                            Glide.get(getContext()).clearDiskCache();
                        }
                    }).start();

                    Log.i("testttt4",result5);
                    break;
            }
        }
    };


    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle){
        View v= View.inflate(getContext(), R.layout.me_fragment,null);
        initView(v);
        addListener();
        requestData();
        return v;
    }
    //添加监听
    protected void addListener(){
        imgHead.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        tvRealname.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        shez.setOnClickListener(this);

    }

    protected void requestData(){
        HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                .getString(Constant.UserId,""),handler);
    }

    //实例化
    protected void initView(View v){
        imgHead = (ImageView) v.findViewById(R.id.img_head);
        tvName = (TextView) v.findViewById(R.id.tv_name_mine);
        tvPhone = (TextView) v.findViewById(R.id.tv_phone_mine);
        tvSex = (TextView)v.findViewById(R.id.tv_sex_mine);
        tvRealname = (TextView) v.findViewById(R.id.tv_realname_mine);
        shez = (LinearLayout) v.findViewById(R.id.shez);
    }
    //数据同步
    protected void analysis(final JsonObject jo){
        handler.post(new Runnable() {
            @Override
            public void run() {
                //      Log.i("mefargment",API.BASEURL+jo.get("UserAvarl").getAsString());
                int signature = 0;
                Glide.clear(imgHead);

                Glide.with(getContext()).load(API.BASEURL+jo.get("UserAvarl").getAsString()).placeholder(R.drawable.groupmanager_icon_one) .into(imgHead);
                tvName.setText(jo.get("NickName").getAsString());
                tvPhone.setText(jo.get("UserPhone").getAsString());
                tvRealname .setText(jo.get("RealName").getAsString());
                tvSex .setText(jo.get("UserSex").getAsString());
            }
        });
    }


    //监听事件处理
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_head:
                showIconDialog(view);
                break;
            case R.id.tv_name_mine:
                Intent intent_name = new Intent(getActivity(),ModifyNameActivity.class);
                startActivityForResult(intent_name,1);
                break;
            case R.id.tv_phone_mine:
                Intent intent_phone = new Intent(getActivity(),ModifyPhoneActivity.class);
                startActivityForResult(intent_phone,2);
                break;
            case R.id.tv_realname_mine:
                Intent intent_realname = new Intent(getActivity(),ModifyRealnameActivity.class);
                startActivityForResult(intent_realname,3);
                break;
            case R.id.tv_sex_mine:
               setSex();
                break;
            case R.id.shez:
                SkipUtils.skipActivity(getContext(),SetActivity.class);
                break;
            case R.id.camera:
                choseHeadImageFromCameraCapture();
                iconDialog_mine.dismiss();
                break;
            case R.id.pic:
                choseHeadImageFromGallery();
                iconDialog_mine.dismiss();
                break;
            case R.id.cancel:
                iconDialog_mine.dismiss();
                break;
            case R.id.name_back_mine:
                super.onDestroy();
                break;

        }
    }


    //初始化头像并弹出对话框方法
    public void showIconDialog(View view){
        iconDialog_mine = new Dialog(getActivity(), R.style.IconDialogTheme_mine);
        //填充对话框的布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.mine_choosephtosbuttom, null);
        //初始化控件
        camera = inflate.findViewById(R.id.camera);
        pic =  inflate.findViewById(R.id.pic);
        cancel =  inflate.findViewById(R.id.cancel);
        camera.setOnClickListener(this);
        pic.setOnClickListener(this);
        cancel.setOnClickListener(this);
        //将布局设置给Dialog
        iconDialog_mine.setContentView(inflate);

        //获取当前Activity所在的窗体
        Window dialogWindow = iconDialog_mine.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;//新坐标X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        inflate.measure(0, 0);
        lp.height = inflate.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        iconDialog_mine.show();//显示对话框
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // 判断存储卡是否可用，存储照片文件
            if (hasSdcard()) {
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
            }
            startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");//选择图片
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置剪裁
        intent.putExtra("crop", "true");
        //设置剪裁的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        //     intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CODE_RESULT_REQUEST);

    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.i("ddd",photo.getHeight()+"");
            imgHead.setImageBitmap(photo);
           // Toast.makeText(getActivity(),"修改头像成功！",Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

//新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory()+"/Ask");
           if (!nf.exists()){
               nf.mkdir();
           }

//在根目录下面的ASk文件夹下 创建okkk.jpg文件
         //   File f = new File(Environment.getExternalStorageDirectory()+"/Ask", "1.jpg");
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

            File file = new File(Environment.getExternalStorageDirectory(),  sdf.format(new Date())+ ".png");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                try {
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
            HashMap<String,Object> m = new HashMap<>();
            m.put("userId",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                    .getString(Constant.UserId,""));
            m.put("File",file);
            Log.i("44",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                    .getString(Constant.UserId,""));
            Log.i("44",file.toString());
            HttpUtil.getInstence().doPostMutliFile(com.example.locationword.locationword.http.API.reAvarl,m,handler,900);

//            FileOutputStream out = null;
//            try {       //打开输出流 将图片数据填入文件中
//                out = new FileOutputStream(f);
//                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
//                try {
//                    out.flush();
//                    out.close();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }else{
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    private void  setSex(){
        int index= 0;
        if(tvSex.getText().toString().equals("男")){
            index = 0;
        }else{
            index=1;
        }
       al =new android.support.v7.app.AlertDialog.Builder(getContext())
                .setSingleChoiceItems(new String[]{"男", "女"}, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            ShowUtil.showText(getContext(),"修改成功！");
                            tvSex.setText(" 男");
                            HashMap<String,String> m = new HashMap<>();
                            m.put("userId",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                                    .getString(Constant.UserId,""));
                            m.put("newSex","男");
                            HttpUtil.getInstence().doPost(com.example.locationword.locationword.http.API.reSex,m,handler,700);
                            al.dismiss();
                        }else{
                            ShowUtil.showText(getContext(),"修改成功！");
                            tvSex.setText(" 女");
                            HashMap<String,String> m = new HashMap<>();
                            m.put("userId",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                                    .getString(Constant.UserId,""));
                            m.put("newSex","女");
                            HttpUtil.getInstence().doPost(com.example.locationword.locationword.http.API.reSex,m,handler,700);
                            al.dismiss();
                        }
                    }
                }).create();
        al.show();
    }

    //数据回传
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //修改昵称
        if(requestCode== 1&& requestCode ==1){
            if (data!=null){
                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                tvName.setText(data.getStringExtra("text"));
                String s =   data.getStringExtra("text");
                HashMap<String,String> m = new HashMap<>();
                m.put("userId",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                        .getString(Constant.UserId,""));
                m.put("newNickname",s);
                HttpUtil.getInstence().doPost(com.example.locationword.locationword.http.API.reNickname,m,handler,500);
            }
        }
        //修改电话
        if(requestCode== 2&& requestCode ==2){
            if (data != null){
                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                tvPhone.setText(data.getStringExtra("text"));
                String s =   data.getStringExtra("text");
                HashMap<String,String> m = new HashMap<>();
                m.put("userId",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                        .getString(Constant.UserId,""));
                m.put("newPhone",s);
                HttpUtil.getInstence().doPost(com.example.locationword.locationword.http.API.reChangePhone,m,handler,800);

            }

        }
        //修改真实姓名
        if(requestCode== 3&& requestCode ==3){
            if (data!= null){
                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                tvRealname.setText(data.getStringExtra("text"));
                String s =   data.getStringExtra("text");
                HashMap<String,String> m = new HashMap<>();
                m.put("userId",getContext().getSharedPreferences(Constant.logindata, MODE_PRIVATE)
                        .getString(Constant.UserId,""));
                m.put("newRealname",s);
                HttpUtil.getInstence().doPost(com.example.locationword.locationword.http.API.reRealname,m,handler,600);
            }

        }
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            //取消
       //     Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        //照相机选择
        if (requestCode == CODE_CAMERA_REQUEST){
            if (hasSdcard()) {
                File tempFile = new File(
                        Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME);
                cropRawPhoto(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG)
                        .show();
            }
        }
        //本地相册选择
        if (requestCode == CODE_GALLERY_REQUEST) {
            //如果是来自本地的
            cropRawPhoto(data.getData());//直接裁剪图片
        }

        if (requestCode == CODE_RESULT_REQUEST){
            if (data != null) {

                setImageToHeadView(data);//设置图片框
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
