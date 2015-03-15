package com.ferreron.modernart;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;


public class ModernArt extends ActionBarActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art);

        // Ease handling any number of rectangles
        final int numRec = 5;
        final int[] ColorsUsed = {getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.white)};
        final int[] TViewsUsed = {R.id.Rect0,
                R.id.Rect1,
                R.id.Rect2,
                R.id.Rect3,
                R.id.Rect4};

        // Set initial colors
        final TextView[] Rects = new TextView[numRec];
        for (int i=0; i<numRec; i++) {
            Rects[i] = (TextView) findViewById(TViewsUsed[i]);
            Rects[i].setBackgroundColor(ColorsUsed[i]);
        }

        // Set the seek bar's listener
        SeekBar SB = (SeekBar) findViewById(R.id.seekBar);
        SB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Change the color according to progress
                float[] rcolor = new float[3];
                for (int i=0; i<numRec; i++) {
                    Color.colorToHSV(ColorsUsed[i], rcolor);
                    if (rcolor[0] > 0) { // avoid white
                        rcolor[0] = rcolor[0] + progress / 2;
                        Rects[i].setBackgroundColor(Color.HSVToColor(rcolor));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modern_art, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.more_information:
                // Create a new AlertDialogFragment
                DialogFragment mDialog = new OpenMomaDialogFragment();
                mDialog.show(getFragmentManager(), "Moma Dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // This class manages the alert dialog
    public static class OpenMomaDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_menu_moma)
                    .setPositiveButton(R.string.dialog_visit_moma, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Create implicit intent to start the browser
                            Uri momaweb = Uri.parse("http://www.moma.org");
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, momaweb);
                            startActivity(webIntent);
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { }
                    });
            return builder.create();
        }
    }
}
