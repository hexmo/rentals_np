package np.com.ranjan.rental.ViewControllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import np.com.ranjan.rental.R;

/**
 * This class is responsible for showing loading dialog.
 */
public class LoadingClass {
    //Objects declaration
    private Activity activity;
    private AlertDialog alertDialog;

    //constructor
    public LoadingClass(Activity activity) {
        this.activity = activity;
    }

    /**
     * This methods starts to show loading dialog
     */
    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loading_layout, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     * This methods hides loading dialog
     */
    public void dismissLoading() {
        alertDialog.dismiss();
    }
}
