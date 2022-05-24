package com.example.teamns_arcore;

import static com.example.teamns_arcore.MainActivity.UserEmail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamns_arcore.Record.ChartActivity;
import com.example.teamns_arcore.SelectLevel.SelectLevelMain;

public class DashboardActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabaseObj; // == private SQLiteDatabase db;
    SQLiteHelper sqLiteHelper;
    //TextView Name;
    ImageButton LogOUT, NameChg, GotoRecord ;
    //Button LogOUT, NameChg ;

    public static final String UserEmail = "";
    public static final String UserId = "";
    String EmailHolder;
    //다이얼로그
    EditText chgName;  // 대화상자 입력값 저장
    String txtNickName;
    LinearLayout dialogPopUp;
    //
    /* 원래 main에 있던 정보들  */
    Button StartBtn, EndBtn;
    //

    public static Boolean ismute =true;
    ImageButton muteBtn;

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

    public static MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //배경음악
        mediaPlayer = MediaPlayer.create(this, R.raw.openning);
        mediaPlayer.seekTo(3000);
        mediaPlayer.setLooping(true);

        //Name = (TextView)findViewById(R.id.textView1);
        LogOUT = (ImageButton)findViewById(R.id.button1);
        NameChg = (ImageButton)findViewById(R.id.namechg);
        GotoRecord = (ImageButton)findViewById(R.id.gorecord);

        // /* 원래 main에 있던 정보들  */
        // 버튼 리스너
        StartBtn = findViewById(R.id.StartBtn); // -> start버튼이 로그인 후 페이지로 가야한다.
        EndBtn = findViewById(R.id.EndBtn);
        muteBtn = findViewById(R.id.muteBtn);
        //StartBtn.setVisibility(View.GONE);
        //EndBtn.setVisibility(View.GONE);
        //findViewById(R.id.regnicknameBtn).setOnClickListener(onClickListener);
        findViewById(R.id.StartBtn).setOnClickListener(onClickListener);
        findViewById(R.id.EndBtn).setOnClickListener(onClickListener);
        findViewById(R.id.muteBtn).setOnClickListener(onClickListener);
        findViewById(R.id.gorecord).setOnClickListener(onClickListener);
        //
        // MainActivity에서 유저id 받기
        Intent intent = getIntent();
        sqLiteHelper = new SQLiteHelper(this);
        EmailHolder = intent.getStringExtra(MainActivity.UserId);
        // TextView에 이름 넣어주기
        //select();
        //System.out.println("dash 에서 EmailHolder : "+ EmailHolder); /
        //System.out.println("select(); : "+select());
        //Name.setText("어서오세요. "+select()+" 님");

        // 로그아웃 버튼
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 클릭 시 현재 DashBoard 활동을 마칩니다.
                //finish();
                //Toast.makeText(DashboardActivity.this,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Logout();
            }
        });

        // 이름 변경 버튼
        NameChg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPopUp = (LinearLayout) View.inflate(DashboardActivity.this, R.layout.rename_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(DashboardActivity.this);
                dlg.setTitle("사용자 닉네임 변경");
                dlg.setView(dialogPopUp); // 대화상자에 뷰 넣음
                chgName = (EditText) dialogPopUp.findViewById(R.id.chgNickName); // edit
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        txtNickName = chgName.getText().toString(); // 수정한 글자 저장
                        if(!(txtNickName.isEmpty())){
                            // 여기서 수정해야함
                            sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
                            // UPDATE UserTable SET name='kimjin' WHERE email = 'test';
                            Cursor uCursor = sqLiteDatabaseObj.rawQuery("UPDATE " + SQLiteHelper.TABLE_NAME + " SET "+ SQLiteHelper.Table_Column_1_Name + " = ' " + txtNickName + "' WHERE "+ SQLiteHelper.Table_Column_2_Email +" = '"+ EmailHolder+"';", null);
                            uCursor.moveToFirst();
                            uCursor.close();
                            System.out.println(uCursor);
                            Toast.makeText(DashboardActivity.this, "변경되었습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DashboardActivity.this, "빈칸이라 변경되지않았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dlg.setNegativeButton("취소",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(DashboardActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setCancelable(false); // 밖에 창 눌러도 안꺼지게
                dlg.show();
            }
        });

    }

    //버튼 클릭 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.regnicknameBtn:
//                    RegNickname();
//                    currentnickname.setText("현재 닉네임 : " + pname);
//                    break;
                case R.id.StartBtn:
                    //난이도 선택
                    //myStartActivity(SelectLevelMain.class);
                    Intent userNameintent = new Intent(DashboardActivity.this, SelectLevelMain.class);
                    userNameintent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    userNameintent.putExtra(UserEmail, EmailHolder);
                    startActivity(userNameintent);
                    break;
                case R.id.EndBtn:
                    //게임종료메서드
                    finish();
                    break;
                case R.id.muteBtn:
                    if(ismute){
                        ismute = false;
                        mediaPlayer.pause();
                        muteBtn.setImageResource(R.drawable.music_icon_off);
                    } else {
                        ismute = true;
                        mediaPlayer.start();
                        muteBtn.setImageResource(R.drawable.music_icon);
                    }
                    break;
                case R.id.gorecord:
                    Intent gotorecord = new Intent(DashboardActivity.this, ChartActivity.class);
                    startActivity(gotorecord);
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Logout();

    }
    public void Logout(){
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 1.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 1.5초가 지났으면 Toast 출력
        // 1500 milliseconds = 1.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "버튼을 한 번 더 누르시면 로그아웃 됩니다", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 1.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 1.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"로그아웃 되었습니다",Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ismute) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}