package com.example.appbanmypham.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.appbanmypham.R;
import com.example.appbanmypham.adapter.ChatAdapter;
import com.example.appbanmypham.model.ChatMessage;
import com.example.appbanmypham.utils.Utils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView imgSend;
    EditText edtMess;
    FirebaseFirestore db;
    ChatAdapter adapter;
    List<ChatMessage> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initControl();
        listenMess();
        inserUser();
    }
//chèn tt vào csdl Firestore
    private void inserUser() {
        HashMap<String, Object> user = new HashMap<>();// khoi tạo HashMap
        //Điền thông tin người dùng vào HashMap
        user.put("id", Utils.user_current.getId());
        user.put("username", Utils.user_current.getUsername());
        //Chèn dữ liệu người dùng vào Firestore
        db.collection("users").document(String.valueOf(Utils.user_current.getId())).set(user);
    }

    private void initControl() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessToFire();
            }
        });
    }

    private void sendMessToFire() {
        //Lấy nội dung tin nhắn từ EditText và xóa khoảng trắng ở đầu và cuối
        String str_mess = edtMess.getText().toString().trim();
        if (TextUtils.isEmpty(str_mess)){

        }else {
            //Tạo một đối tượng HashMap mới có tên là message
            HashMap<String, Object> message = new HashMap<>();
            message.put(Utils.SENDID, String.valueOf(Utils.user_current.getId()));
            message.put(Utils.RECEIVEDID,Utils.ID_RECEIVED);
            message.put(Utils.MESS, str_mess);
            message.put(Utils.DATETIME, new Date());
            //Chèn dữ liệu tin nhắn vào Firestore
            db.collection(Utils.PATH_CHAT).add(message);
            edtMess.setText("");

        }
    }
    //Lắng nghe các tin nhắn chat mới được gửi hoặc nhận
    private void listenMess(){
        //Chọn bộ sưu tập có tên được lưu trong Utils.PATH_CHAT
        db.collection(Utils.PATH_CHAT)
                //Lọc các tài liệu trong bộ sưu tập nơi giá trị của trường SENDID bằng với ID của người dùng hiện tại.
                .whereEqualTo(Utils.SENDID, String.valueOf(Utils.user_current.getId()))
                .whereEqualTo(Utils.RECEIVEDID,Utils.ID_RECEIVED)
                //Thêm một SnapshotListener để lắng nghe các thay đổi trong tập hợp kết quả
                .addSnapshotListener(eventListener);

        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,Utils.ID_RECEIVED)
                .whereEqualTo(Utils.RECEIVEDID,String.valueOf(Utils.user_current.getId()))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        }
        if (value != null){
            int count = list.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.sendid = documentChange.getDocument().getString(Utils.SENDID);
                    chatMessage.receivedid = documentChange.getDocument().getString(Utils.RECEIVEDID);
                    chatMessage.mess = documentChange.getDocument().getString(Utils.MESS);
                    chatMessage.dateObj = documentChange.getDocument().getDate(Utils.DATETIME);
                    chatMessage.datetime = format_date(documentChange.getDocument().getDate(Utils.DATETIME));
                    list.add(chatMessage);
                }
            }
            Collections.sort(list, (obj1, obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
            if (count == 0){
                adapter.notifyDataSetChanged();
            }else {
                adapter.notifyItemRangeInserted(list.size(),list.size());
                recyclerView.smoothScrollToPosition(list.size()-1);

            }
        }
    };
    private String format_date(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy- hh:mm a", Locale.getDefault()).format(date);
    }

    private void initView() {
        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycleview_chat);
        imgSend = findViewById(R.id.imagechat);
        edtMess = findViewById(R.id.edtinputtex);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(getApplicationContext(),list,String.valueOf(Utils.user_current.getId()));
        recyclerView.setAdapter(adapter);

    }
}