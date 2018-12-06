package com.example.locationword.locationword;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView i_head,i_information,i_group;
    TextView t_name,t_phone,t_realname,t_age,t_sex;
    LinearLayout layout_realname,layout_age,layout_sex;
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout);

        i_head = (ImageView)findViewById(R.id.img_head);
        i_information = (ImageView)findViewById(R.id.img_information);
        i_group = (ImageView)findViewById(R.id.img_group);

        t_name = (TextView)findViewById(R.id.tv_name);
        t_phone = (TextView)findViewById(R.id.tv_phone);
        t_realname = (TextView)findViewById(R.id.tv_realname);
        t_age = (TextView)findViewById(R.id.tv_age);
        t_sex = (TextView) findViewById(R.id.tv_sex);

        layout_realname = (LinearLayout)findViewById(R.id.realname_layout);
        layout_age = (LinearLayout)findViewById(R.id.age_layout);
        layout_sex = (LinearLayout)findViewById(R.id.sex_layout);



        i_head.setOnClickListener(this);
        t_name.setOnClickListener(this);
        t_phone.setOnClickListener(this);
        layout_realname.setOnClickListener(this);
        layout_age.setOnClickListener(this);
        layout_sex.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.img_head:
                //修改头像
                showTypeDialog();
                break;
            case R.id.img_information:
                //跳转到聊天页
                break;
            case R.id.img_group:
                //跳转到群组页
                Intent intent = new Intent(this,ModifyNameActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_name:
                //修改昵称
                Log.i("666","昵称");
                Intent intent1 = new Intent(this,ModifyNameActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_phone:
                //修改电话号码
                Log.i("666","号码");
                Intent intent2 = new Intent(this,ModifyPhoneActivity.class);
                startActivity(intent2);
                break;
            case R.id.realname_layout:
                //修改真实姓名
                Log.i("666","真实姓名");
                Intent intent3 = new Intent(this,ModifyRealnameActivity.class);
                startActivity(intent3);
                break;
            case R.id.age_layout:
                //修改年龄
                Log.i("666","年龄");
                Intent intent4 = new Intent(this,ModifyAgeActivity.class);
                startActivity(intent4);
                break;
            case R.id.sex_layout:
                //修改性别
                Log.i("666","性别");
                new AlertDialog.Builder(this).setTitle("请选择性别").setIcon(R.mipmap.ic_launcher).setSingleChoiceItems(new String[]{"男","女"},0,new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialogInterface, int which){
                }}).setPositiveButton("确定",null).show();
                break;
        }
    }

    private void showTypeDialog() {
        //显示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                //打开文件
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    i_head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        i_head.setImageBitmap(head);// 用ImageButton显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
