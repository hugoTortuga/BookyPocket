package android.cnam.bookypocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;

public class ReadingsActivity extends AppCompatActivity {
    private MutableLiveData<String> mText;

    public ReadingsActivity() {
        mText = new MutableLiveData<>();
        mText.setValue("This is readings activity");
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

    }


}