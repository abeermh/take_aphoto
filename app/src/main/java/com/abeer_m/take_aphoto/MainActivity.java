package com.abeer_m.take_aphoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    ImageButton b;
    ImageView img;
    Uri file;
    PopupMenu popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(ImageButton)findViewById(R.id.imageButton) ;
        img=(ImageView)findViewById(R.id.imageview);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);

        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whenclicked();
            }
        });
    }

    public void takeaphoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         file = Uri.fromFile(getOutputMediafile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, 1);
    }

    private static File getOutputMediafile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "camera");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;}}
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public void openGallary(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case 1:
                    img.setImageURI(file); break;
                case 2:
                    Uri selectimg=data.getData();
                    try{
                        Bitmap b=MediaStore.Images.Media.getBitmap(getContentResolver(),selectimg);
                        img.setImageBitmap(b);
                    }catch (IOException e){
                        Log.i("TAG", "onActivityResult: "+e);
                    }break;

            }

        }}

    public void whenclicked() {
        popup = new PopupMenu(MainActivity.this, b);
        popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()== R.id.camera){
                    takeaphoto();

                }else if(item.getItemId()==R.id.gallary){
                   openGallary();
                }else{
                    Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.white);
                    img.setImageBitmap(icon);
                 }
             return true;}
        });

        popup.show();
    }


}
