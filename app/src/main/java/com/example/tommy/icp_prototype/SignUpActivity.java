package com.example.tommy.icp_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getName();

    private String uid;
    private String name, nickname, gender, address, phone;
    private EditText nameEdit, nicknameEdit, genderEdit, addressEdit, phoneEdit;
    private Button submitBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEdit = (EditText)findViewById(R.id.sign_up_name_edit);
        nicknameEdit = (EditText)findViewById(R.id.sign_up_nickname_edit);
        genderEdit = (EditText)findViewById(R.id.sign_up_gender_edit);
        addressEdit = (EditText)findViewById(R.id.sign_up_address_edit);
        phoneEdit = (EditText)findViewById(R.id.sign_up_phone_edit);
        submitBtn = (Button)findViewById(R.id.btn_submit);

        uid = getIntent().getStringExtra("uid");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdit.getText().toString();
                nickname = nicknameEdit.getText().toString();
                gender = genderEdit.getText().toString();
                address = addressEdit.getText().toString();
                phone = phoneEdit.getText().toString();

                if (name.isEmpty()
                        ||nickname.isEmpty()
                        ||gender.isEmpty()
                        ||address.isEmpty()
                        ||phone.isEmpty()){

                }else{
                    // 모든 정보를 입력했으면, 정보를 Firestore Database에 올려주고, MainActivity 실행

                    // Create user map
                    Map<String, Object> user = new HashMap<>();
                    user.put("관리자", false);
                    user.put("닉네임", nickname);
                    user.put("매니저", false);
                    user.put("성별", gender);
                    user.put("주소", address);
                    user.put("이름", name);
                    user.put("휴대전화", phone);

                    // Add a new document with a generated ID
                    db.collection("회원")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                    // 구매내역 collection 추가
                                    db.collection("회원")
                                            .document(uid)
                                            .collection("구매내역");

                                    // 장바구니 collection 추가
                                    db.collection("회원")
                                            .document(uid)
                                            .collection("장바구니");

                                    Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                    mainIntent.putExtra("uid",uid);
                                    startActivity(mainIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "DocumentSnapshot failed to be written!");
                                }
                            });
                }
            }
        });


    }
}
