package android.cnam.bookypocket.ui.bookyMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookyMapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BookyMapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}