package net.hbusstu.wastesorting.KnowledgePart;

import android.os.Bundle;



import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hbusstu.wastesorting.R;

public class FirstFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) { //调用savaInstanceState（）来保存当前的状态信息。
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

}