package com.example.igi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import static com.example.igi.R.id.info_long;

public class InfoPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);
        TextView txtDetails = (TextView) findViewById(info_long);
        txtDetails.setText("IGI's Idea\n" +
                "\n" +
                "IGI established to give the world an easy and simple way to bring out solutions to daily problems that everyone is dealing with.\n" +
                "\n" +
                "Why We Are Here?\n" +
                "\n" +
                "Today, the way entrepreneurs need to go through to get to the market and start spreading his idea to the world, as a product, an app or any other way, is so complicated that about 99% of the startups are falling!\n" +
                "\n" +
                "The Main Problem\n" +
                "\n" +
                "After we did research, we realized that the reason people stop developing their great idea is mostly short resources - yes, Google isn't enough.\n" +
                "\n" +
                "The Solution - IGI\n" +
                "\n" +
                "IGI is here to simplify and improve the process, start with finding a problem, through suggest solutions and developing prototype, to raising money and start selling it. All based on the power of our global internet village.\n" +
                "\n" +
                "Our Logo\n" +
                "\n" +
                "The iconic cow-rocket designed to remind everyone that IGI's target is that even a low-cognition animal like a cow - can become the next huge startup.");
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
    }
}
