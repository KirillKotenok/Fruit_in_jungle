package com.jungleink.fruitinjungle;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String SCORE_KEY = "SCORE";
    private TextView textView;
    private ImageButton start_btn;
    private ImageButton exit_btn;
    private Intent intent;
    private Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        textView = findViewById(R.id.welcome);
        start_btn = findViewById(R.id.start_btn);
        exit_btn = findViewById(R.id.exit_btn);
        start_btn.setOnClickListener(v -> {
            setContentView(new GameView(this, point.x, point.y));
        });
        exit_btn.setOnClickListener(v -> {
            finish();
        });
    }

    public void startEndActivity(int score) {
        intent = new Intent(this, EndGameActivity.class);
        intent.putExtra(SCORE_KEY, score);
        startActivity(intent);
        finish();
    }

}