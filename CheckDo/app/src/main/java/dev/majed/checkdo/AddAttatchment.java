package dev.majed.checkdo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.wunderlist.slidinglayer.SlidingLayer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.paperdb.Paper;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddAttatchment extends AppCompatActivity {

    private static final int REQUEST_CAMERA =12345 ;
    LinearLayout Camera;
    LinearLayout Gallery;
    LinearLayout Video;
    LinearLayout AnyFile;
    LinearLayout Audio;
    LinearLayout Record;

Button backBtn;
    static AttatchmentAdapter attatchmentAdapter;
    private String mCurrentPhotoPath;
    static ArrayList<SingleAttatchment> list = new ArrayList<>();
   static String id;
    private EditText NotesText;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attatchment_activity);
        final SlidingLayer slidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Paper.init(this);

        NotesText = (EditText)findViewById(R.id.note_edit_text);
        Hawk.init(this).build();

        backBtn =(Button)findViewById(R.id.button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        id = String.valueOf(getIntent().getLongExtra("id",0));

        list = Hawk.get(id);
        String Notes = Hawk.get(id+"notes","");
        NotesText.setText(Notes);
        //  list = Paper.book().read(id);
     //   list = Paper.book().read("attatchmentData", new ArrayList<SingleAttatchment>());
        Log.e("Id",id);
        if(list == null){
            list = new ArrayList<>();
        } Log.e("Size",String.valueOf(list.size()));
      /* try{}
       catch(Exception exp){Log.e("error","error reading attatchment data");}

*/
      NotesText.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              Hawk.put(id+"notes", editable.toString());

          }
      });

        attatchmentAdapter = new AttatchmentAdapter(list,this);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list);
        GridLayoutManager grid = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(grid);
        recyclerView.setAdapter(attatchmentAdapter);
     //   Log.e("error",String.valueOf(list.size()));

        Camera = (LinearLayout) findViewById(R.id.event_1_1);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)||(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)||(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)){
                    openCameraRequest();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(AddAttatchment.this, "Camera permission is required to capture the image.", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},CAMERA_TYPE);

                }

            }
        });

        Gallery = (LinearLayout) findViewById(R.id.event_1_2);
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    UploadFromGallery();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAttatchment.this, "Please give permission to read data.", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_TYPE);


                }
            }
        });

        Video = (LinearLayout) findViewById(R.id.event_1_3);
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    AddVideo();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAttatchment.this, "Please give permission to read data.", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},VIDEO_TYPE);


                }
            }
        });

        AnyFile = (LinearLayout) findViewById(R.id.event_1_4);
        AnyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    AddFile();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAttatchment.this, "Please give permission to read data.", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},FILE_TYPE);


                }
            }
        });

        Audio = (LinearLayout) findViewById(R.id.event_1_5);
        Audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                   try{ AddAudio();}
                   catch (Exception e){}
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAttatchment.this, "Please give permission to read data.", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},AUDIO_TYPE);


                }
            }
        });
        Record = (LinearLayout) findViewById(R.id.event_1_6);
        Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
                    RecordAudio();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAttatchment.this, "Please give permission to read data.", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.WAKE_LOCK},RECORD_TYPE);


                }
            }
        });
    }


public int code =0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode== FilePickerConst.REQUEST_CODE_DOC && code==1){
                post(Uri.fromFile(new File(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0))),FILE_TYPE);
                code=0;
            }
            else if(requestCode== FilePickerConst.REQUEST_CODE_DOC && code==2){
                if((data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0)).contains("mp3"))
                {  post(Uri.fromFile(new File(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).get(0))),AUDIO_TYPE);}
                else{
                    Toast.makeText(this, "This file is not supported as an audio file. Please attach it from file tab.", Toast.LENGTH_SHORT).show();
                }
                code=0;
            }
           // else if(requestCode == FilePickerConst.RE )
       else if(requestCode!=RECORD_TYPE )   {
               // Log.e("1",data.getData().getEncodedPath()+" ");
               // Log.e("3",getPath(data.getData())+" ");
               // Log.e("2",data.getData().getPath()+" ");
               // Log.e("4",data.getData().getQuery()+" ");

                if (data != null) {
                    post(data.getData(), requestCode);
                } else {
                    Toast.makeText(this, "Something's wrong! Please try again. ", Toast.LENGTH_SHORT).show();
                }
            }

            else{
             SingleAttatchment singleAttatchment  = new SingleAttatchment(RECORD_TYPE,AUDIO_FILE_NAME );
             list.add(singleAttatchment);
             attatchmentAdapter.notifyDataSetChanged();
             save();
         }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_TYPE){openCameraRequest();}
        else if (requestCode == GALLERY_TYPE){UploadFromGallery();}
        else if (requestCode==VIDEO_TYPE){AddVideo();}
        else if (requestCode==AUDIO_TYPE){AddAudio();}
        else if (requestCode==FILE_TYPE){AddFile();}
        else if (requestCode==RECORD_TYPE){RecordAudio();}
    }
    static String AUDIO_FILE_NAME = "";



public void RecordAudio(){

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    AUDIO_FILE_NAME = Environment.getExternalStorageDirectory() +"/"+ timeStamp+"recorded_audio.mp3";
    int color = getResources().getColor(R.color.colorPrimaryDark);
    int requestCode = 0;
    AndroidAudioRecorder.with(this)
            // Required
            .setFilePath(AUDIO_FILE_NAME)
            .setColor(color)
            .setRequestCode(RECORD_TYPE)

            // Optional
            .setSource(AudioSource.MIC)
            .setChannel(AudioChannel.STEREO)
            .setSampleRate(AudioSampleRate.HZ_48000)
            .setAutoStart(true)
            .setKeepDisplayOn(true)

            // Start recording
            .record();
    }

    private void AddVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),VIDEO_TYPE);
    }
    private ArrayList<String> docPaths = new ArrayList<>();

    private void AddFile() {
code=1;
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(docPaths)

                .setActivityTheme(R.style.AppTheme)
                .pickFile(this);


       // Intent intent = new Intent();
       // intent.setType("*/*");
       // intent.setAction(Intent.ACTION_GET_CONTENT);
     //   startActivityForResult(Intent.createChooser(intent,"Select file"),FILE_TYPE);
    }
String[] audioFormat  = {"mp3"};
    private void AddAudio() {
        code=2;
        String[] zipTypes = {".zip",".rar"};

        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(docPaths)
                .setActivityTheme(R.style.AppTheme)
                .addFileSupport("audio",audioFormat)
       . addFileSupport("ZIP",zipTypes, R.drawable.audio_file)
                .pickFile(this);
    }

    private void UploadFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"Select image"),GALLERY_TYPE );
    }

    private void AddText() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_TYPE);
    }
    private void post(Uri uri, int type) {
      //  File file = new File(uri.getPath());
     //   File file=null;
     // file = new File(getRealPathFromURI_API19(this, uri));
        SingleAttatchment singleAttatchment = null;
if(type==GALLERY_TYPE) {
    try {
        singleAttatchment  = new SingleAttatchment(type, getStrPath(this,uri));
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
}
else if(type==CAMERA_TYPE) {
    Uri uri2 = Uri.fromFile(photoFile);
    singleAttatchment  = new SingleAttatchment(type, uri2.getPath());
}
else if(type==VIDEO_TYPE){
Log.e("pathFetched",getPath(uri));
    singleAttatchment  = new SingleAttatchment(type, getPath(uri));
}
else if(type==AUDIO_TYPE){
     singleAttatchment  = new SingleAttatchment(type, (uri).getPath());
}
else if(type==FILE_TYPE){
    Log.e("data",String.valueOf(uri));
    Log.e("data",String.valueOf(uri.getPath()));
    Log.e("data",String.valueOf(uri.getEncodedPath()));
    Log.e("data",String.valueOf(uri.getLastPathSegment()));

    singleAttatchment  = new SingleAttatchment(type,uri.getPath());

}
else if(type==RECORD_TYPE){
    singleAttatchment  = new SingleAttatchment(type,uri.getPath());

}

        //Log.e("Uri",String.valueOf(uri.getPath()));
//        Log.e("file",String.valueOf(file.getPath()));

        list.add(singleAttatchment);
        attatchmentAdapter.notifyDataSetChanged();
        save();

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

 /*   private void Cpost(Uri uri, int type) {
        //  File file = new File(uri.getPath());
      //  File file=null;
      //  file = new File(getRealPathFromURI_API19(this, uri));


        SingleAttatchment singleAttatchment = new SingleAttatchment(type,getRealPathFromURI_API19(this,uri));

        Log.e("Uri",String.valueOf(uri.getPath()));
     //   Log.e("file",String.valueOf(file.getPath()));

        list.add(singleAttatchment);
        attatchmentAdapter.notifyDataSetChanged();
        save();

    */

 public static String getStrPath(Context context, Uri uri) throws URISyntaxException {
     if ("content".equalsIgnoreCase(uri.getScheme())) {
         String[] projection = { "_data" };
         Cursor cursor = null;

         try {
             cursor = context.getContentResolver().query(uri, projection, null, null, null);
             int column_index = cursor.getColumnIndexOrThrow("_data");
             if (cursor.moveToFirst()) {
                 return cursor.getString(column_index);
             }
         } catch (Exception e) {
             // Eat it
         }
     }
     else if ("file".equalsIgnoreCase(uri.getScheme())) {
         return uri.getPath();
     }

     return null;
 }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";

        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static void save(){
        Hawk.put(id, list);
      //  Paper.book().write("attatchmentData", list);
        Log.e("reached here",String.valueOf(list.size()));

    }
    public static void remove(int position){
        list.remove(position);
        attatchmentAdapter.notifyDataSetChanged();
        save();

    }

    public File photoFile;
    public String destination;
    public File audioFile;
    public String AudioDestination;

    private void openCameraRequest() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {

            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("error", "IOException");
            }
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CAMERA_TYPE);
                CameraFile = photoFile;
            }
        }
    }
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg ",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        destination = mCurrentPhotoPath;

        return image;
    }
     public static File CameraFile;

    public static int CAMERA_TYPE = 1;
    public static int GALLERY_TYPE = 2;
    public static int VIDEO_TYPE = 3;
    public static int FILE_TYPE = 4;
    public static int AUDIO_TYPE = 5;
    public static int RECORD_TYPE = 6;



}



