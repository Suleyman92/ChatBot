package com.example.asistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView asist;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private  final String BOT_KEY = "bot";
    private  final String USER_KEY = "user";
    private ArrayList<ChatsModal>chatsModalsArraylist;
    private ChatRVAdapter chatRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asist = findViewById(R.id.asist);
        userMsgEdt = findViewById(R.id.EdtMsg);
        sendMsgFAB = findViewById(R.id.AsSend);
        chatsModalsArraylist = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModalsArraylist,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        asist.setLayoutManager(manager);
        asist.setAdapter(chatRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Lütfen Mesajınızı girin",Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });
    }
    private void getResponse(String message){
        chatsModalsArraylist.add(new ChatsModal(message,USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=171139&key=VglukouxkSJqw2TC&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMEssage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if (response.isSuccessful()){
                    MsgModal modal = response.body();
                    chatsModalsArraylist.add(new ChatsModal(modal.getCnt(),BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatsModalsArraylist.add(new ChatsModal("Lütfen sorunuzu yeniden yazın",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });


    }
}