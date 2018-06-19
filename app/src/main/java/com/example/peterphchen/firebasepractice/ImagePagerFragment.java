package com.example.peterphchen.firebasepractice;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImagePagerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_layout,container,false);
        int postion = getArguments().getInt("number");
        ArrayList<String> path = getArguments().getStringArrayList("array");
        ImageView imageSwapper = result.findViewById(R.id.ImageSwapper);
        imageSwapper.setImageBitmap(BitmapFactory.decodeFile(path.get(postion)));
        return result;
    }

    public static ImagePagerFragment newInstance(int position, ArrayList<String> list) {
        Bundle args = new Bundle();
        args.putInt("number",position);
        args.putStringArrayList("array",list);
        ImagePagerFragment fragment = new ImagePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
