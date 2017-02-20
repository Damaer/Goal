package cn.goal.goal;

/**
 * Created by 97617 on 2017/2/16.
 */
import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;

import static cn.goal.goal.R.*;

public class Realtime_statistics_count_fragment extends Fragment {
    private TextView time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // TODO Auto-generated method stub
        View view = inflater.inflate(layout.fragement_count_and_analyse, null);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        time=(TextView) view.findViewById(R.id.time);
        time.setText(year+"年"+month+"月");
        return view;
    }

}
