package kangkan.developer.uploadImagetoserver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText img_title;
    Button button_chose,btn_upload;
    ImageView imgview;
    private  static final int IMG_REQUEST=777;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_title=findViewById(R.id.ext_text);
        button_chose=findViewById(R.id.select_button);
        btn_upload=findViewById(R.id.upload_button);
        imgview=findViewById(R.id.img_view);

        btn_upload.setOnClickListener(this);
        button_chose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.select_button:

                selectImage();
                break;

            case R.id.upload_button:
                String image=imageToString();
                String title=img_title.getText().toString().toLowerCase();
                uploadImage(title,image);
                break;
        }

    }
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    private void uploadImage(String title,String image){


        ApiInterface apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClass> call=apiInterface.uploadImage(title,image);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass=response.body();
                Toast.makeText(MainActivity.this, "Hoisa", Toast.LENGTH_SHORT).show();
                imgview.setVisibility(View.GONE);
                img_title.setVisibility(View.GONE);
                button_chose.setEnabled(true);
                btn_upload.setEnabled(false);
                img_title.setText("");
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri path=data.getData();

            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgview.setImageBitmap(bitmap);
                imgview.setVisibility(View.VISIBLE);
                img_title.setVisibility(View.VISIBLE);
                btn_upload.setEnabled(true);
                button_chose.setEnabled(true);
            }catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }


    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
}