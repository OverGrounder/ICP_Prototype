package com.example.tommy.icp_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.kakao.auth.Session;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private Button testBtn;

    private static final String TAG = SignInActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testBtn = (Button)findViewById(R.id.test_btn);

        final String kakaoAccessToken = Session.getCurrentSession().getTokenInfo().getAccessToken();

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyPayment(kakaoAccessToken).addOnCompleteListener(new OnCompleteListener<String>(){
                    @Override
                    public void onComplete(@NonNull Task<String> task){
                        String next_redirect_app_url = task.getResult();
                        Intent intent = new Intent(MainActivity.this, KakaopayActivity.class);
                        intent.putExtra("next_redirect_app_url", next_redirect_app_url);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private Task<String> readyPayment(final String kakaoAccessToken) {
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.validation_server_domain) + "/readyPayment";
        HashMap<String, Object> validationObject = new HashMap<>();
        validationObject.put("kakao_access_token", kakaoAccessToken);
        validationObject.put("partner_order_id", "123456789abcdef");
        validationObject.put("partner_user_id","kakao:905756637");
        validationObject.put("item_name","BH-4JK04-B_BE_F");
        validationObject.put("quantity",3);
        validationObject.put("total_amount", 25000);
        validationObject.put("tax_free_amount", 0);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(validationObject), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String redirect_url = response.getString("next_redirect_app_url");
                    source.setResult(redirect_url);
                } catch (Exception e) {
                    source.setException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                source.setException(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kakao_access_token", kakaoAccessToken);
                return params;
            }
        };

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
        return source.getTask();
    }

}
