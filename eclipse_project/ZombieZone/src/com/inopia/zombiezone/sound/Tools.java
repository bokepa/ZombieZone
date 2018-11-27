package com.inopia.zombiezone.sound;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.inopia.zombiezone.R;

public class Tools {

	   /**
     * Launch web browser
     */
    static public void launchBrowser(Context ctx, String url) {
    	ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));   
    }
    
    
    public static void MessageBox( final Context ctx, final String text) {
    	Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
    }
    
    /**
     * Helper Alert
     * @param title
     * @param message
     */
    public static void AlertDialog(final Context ctx, final CharSequence title, final CharSequence message) {
		new AlertDialog.Builder(ctx)
		.setIcon(R.drawable.bad1)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
		})
		.show();
    }
}
