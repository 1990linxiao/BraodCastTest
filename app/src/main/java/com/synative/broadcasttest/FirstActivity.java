package com.synative.broadcasttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class FirstActivity extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void startNext(View view) {
    startActivity(new Intent(this,MainActivity.class));
  }

}
