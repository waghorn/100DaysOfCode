package com.christopherwaghorn.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.christopherwaghorn.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.christopherwaghorn.geoquiz.answer_shown";
    private static final String TAG = "CheatActivity";
    private static final String KEY_SHOWN = "shown";

    private boolean mAnswerIsTrue;
    private boolean mAnswerIsShown;
    private Button mShowAnswer;
    private TextView mAnswerTextView;
    private TextView mSDKLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mSDKLevelTextView = findViewById(R.id.sdk_version);

        String SDKText = "API Level " + Build.VERSION.SDK_INT;
        mSDKLevelTextView.setText(SDKText);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = findViewById(R.id.answer_text_view);

        mShowAnswer = findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswerIsShown = true;
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth();
                    int cy = mShowAnswer.getHeight();
                    float radius = mShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (savedInstanceState != null) {
            mAnswerIsShown = savedInstanceState.getBoolean(KEY_SHOWN, false);
            setAnswerShownResult(mAnswerIsShown);
        }
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSavedInstanceState() called");
        savedInstanceState.putBoolean(KEY_SHOWN, mAnswerIsShown);
    }
}
