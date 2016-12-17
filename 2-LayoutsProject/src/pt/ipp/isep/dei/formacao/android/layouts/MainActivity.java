package pt.ipp.isep.dei.formacao.android.layouts;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	public static final String[] items = new String[] {"Portugal", "Espanha", "Brasil"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;

        // Iniciar tab LinearLayout
        tabSpec = tabHost.newTabSpec("LinearLayout");
        tabSpec.setIndicator("LinearLayout", getResources().getDrawable(R.drawable.ic_tab));
        tabSpec.setContent(new Intent().setClass(MainActivity.this, LinearLayoutActivity.class));
        tabHost.addTab(tabSpec);

        // Iniciar tab RelativeLayout
        tabSpec = tabHost.newTabSpec("RelativeLayout");
        tabSpec.setIndicator("RelativeLayout", getResources().getDrawable(R.drawable.ic_tab));
        tabSpec.setContent(new Intent().setClass(MainActivity.this, RelativeLayoutActivity.class));
        tabHost.addTab(tabSpec);        

        // Iniciar tab TableLayout
        tabSpec = tabHost.newTabSpec("TableLayout");
        tabSpec.setIndicator("TableLayout", getResources().getDrawable(R.drawable.ic_tab));
        tabSpec.setContent(new Intent().setClass(MainActivity.this, TableLayoutActivity.class));
        tabHost.addTab(tabSpec);   
    }
}