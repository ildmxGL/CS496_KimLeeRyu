package com.madcamp.test01012012;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment {

    Button ToBlack;
    Button ToWhite;
    Button ToRed;
    Button ToYellow;
    Button ToGreen;
    Button ToBlue;

    Button takeSc;
    Button LoadG;
    ImageView imageView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    MyCanvas myCanvas;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);

        myCanvas = (MyCanvas) view.findViewById(R.id.myCanvas) ;

        takeSc = (Button) view.findViewById(R.id.takeSc);
        LoadG = (Button) view.findViewById(R.id.LoadG);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        final TextView main_label = (TextView) view.findViewById(R.id.main_label);

        takeSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                CustomDialog customDialog = new CustomDialog(getActivity());
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(main_label);
                final String pic_name = (String) main_label.getText();

                final ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.rela);

                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Bitmap pic = takeScreenShot(layout);

                        try {
                            if (pic != null) {
                                saveScreenShot(pic, pic_name);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        LoadG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        ToBlack = (Button) view.findViewById(R.id.ToBlack);
        ToRed = (Button) view.findViewById(R.id.ToRed);
        ToWhite = (Button) view.findViewById(R.id.ToWhite);
        ToYellow = (Button) view.findViewById(R.id.ToYellow);
        ToGreen = (Button) view.findViewById(R.id.ToGreen);
        ToBlue = (Button) view.findViewById(R.id.ToBlue);


        ToBlack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCanvas.paint.setColor(Color.BLACK);
            }
        });
        ToRed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCanvas.paint.setColor(Color.RED);
            }
        });
        ToWhite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCanvas.paint.setColor(Color.GRAY);
            }
        });
        ToYellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCanvas.paint.setColor(Color.YELLOW);
            }
        });
        ToGreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCanvas.paint.setColor(Color.GREEN);
            }
        });
        ToBlue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myCanvas.paint.setColor(Color.BLUE);
            }
        });

        return view;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    //TAKE SCREENSHOT
    private Bitmap takeScreenShot(View v){
        Bitmap screenShot = null;
        try
        {
            //GET WIDTH AND HEIGHT OF VIEW
            int width = v.getMeasuredWidth();
            int height = v.getMeasuredWidth();

            screenShot = Bitmap.createBitmap(width,  height, Bitmap.Config.ARGB_8888);

            //DRAW TO CANVAS
            Canvas c = new Canvas(screenShot);
            v.draw(c);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return screenShot;
    }

    //SAVE TO EXTERNAL STORAGE
    private void saveScreenShot(Bitmap bm, String name){
        ByteArrayOutputStream bao = null;
        File file=null;
        try {
            //COMPRESS AND WRITE TO OUTPUT STREAM
            bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 40, bao);

            //CHECK WHETHER THERE ALREADY EXISTS A FILE WITH SAME NAME AND SAVE THE IMAGE FILE
            Integer counter = 0;


            file = new File(Environment.getExternalStorageDirectory()+"/DCIM/painted" + File.separator+ (name + ".png"));
            while (file.exists()) {
                counter++;
                file = new File(Environment.getExternalStorageDirectory() +"/DCIM/painted" + File.separator + (name + counter + ".png"));
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bao.toByteArray());
            fos.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getActivity().sendBroadcast(intent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
