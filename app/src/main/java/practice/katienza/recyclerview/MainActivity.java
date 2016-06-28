package practice.katienza.recyclerview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements StartFragment.OnClickStartListener {

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container,new StartFragment()).commit();
    }

    @Override
    public void onClickStart() {
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container,new BoardFragment()).commit();
    }
}
