package com.ecochallenge.sns_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecochallenge.sns_project.PostInfo;
import com.ecochallenge.sns_project.R;
import com.ecochallenge.sns_project.UserInfo;
import com.ecochallenge.sns_project.activity.WritePostActivity;
import com.ecochallenge.sns_project.adapter.UserListAdapter;
import com.ecochallenge.sns_project.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class UserListFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private UserListAdapter userListAdapter;
    private ArrayList<UserInfo> userList;
    private boolean updating;
    private boolean topScrolled;
    private int cnt;
    public UserListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_user_list, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        userListAdapter = new UserListAdapter(getActivity(), userList);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if(newState == 1 && firstVisibleItemPosition == 0){
                    topScrolled = true;
                }
                if(newState == 0 && topScrolled){
                    postsUpdate(true,view);
                    topScrolled = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
                    postsUpdate(false, view);
                }

                if(0 < firstVisibleItemPosition){
                    topScrolled = false;
                }
            }
        });

        postsUpdate(false,view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floatingActionButton:
                    myStartActivity(WritePostActivity.class);
                    break;
            }
        }
    };

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            userList.remove(postInfo);
            userListAdapter.notifyDataSetChanged();

            Log.e("로그: ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그: ","수정 성공");
        }
    };

    private void postsUpdate(final boolean clear,final View view) {
        updating = true;
        firebaseFirestore.collection("users").orderBy("score",Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                userList.clear();
                            }

                            cnt=0;
                            TextView myName=(TextView) view.findViewById(R.id.rankingMyNameTextView);
                            TextView myScore=(TextView) view.findViewById(R.id.rankingMyScoreTextView);
                            TextView nextLevel = (TextView) view.findViewById(R.id.nextLevelText);
                            final TextView myTier=(TextView) view.findViewById(R.id.rankingMyTierTextView);
                            TextView myRank=(TextView) view.findViewById(R.id.rankingMyRankTextView);
                            ImageView myImg = (ImageView) view.findViewById(R.id.imageView4);
                            ImageView myTierImageView = (ImageView) view.findViewById(R.id.myTierImageView);
                            String currentUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            ProgressBar levelBar = (ProgressBar) view.findViewById(R.id.levelBar);
                            int value;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cnt=cnt+1;
                                if(document.getId().equals(currentUid))
                                {
                                    value = Math.toIntExact(document.getLong("score"));
                                    myName.setText(document.getData().get("name").toString());
                                    myRank.setText(String.valueOf(cnt));
                                    myScore.setText(document.getLong("score").toString());
                                    if (document.getData().get("photoUrl") != null)
                                        Glide.with(getActivity()).load(document.getData().get("photoUrl")).centerCrop().override(500).into(myImg);
                                    makeTier(myTier, myTierImageView, nextLevel, levelBar, value, view);
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userList.add(new UserInfo(
                                        document.getData().get("name").toString(),
                                        document.getData().get("phoneNumber").toString(),
                                        document.getData().get("birthDay").toString(),
                                        document.getData().get("address").toString(),
                                        document.getData().get("photoUrl") == null ? null : document.getData().get("photoUrl").toString(),
                                        document.getLong("score"),
                                        cnt
                                        ));//
                            }
                            userListAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }

    public void makeTier(TextView myTier, ImageView myTierImageView, TextView nextLevel, ProgressBar levelBar, int value, View view) {
        String tier;
        String levelUp;
        int photo = R.drawable.ic_tier_seed;

        if (value >= 100) {
            levelBar.setMax(100);
            levelBar.setProgress(value);
            TextView text = (TextView) view.findViewById(R.id.nextTextEdit);
            text.setText("최고등급에");
            levelUp = "도달하셨습니다!";
            photo = R.drawable.ic_tier_earth;
            tier = "지구";
        }
        else if (value >= 50) {
            levelBar.setMax(50);
            levelBar.setProgress(value - 50);
            levelUp = (value-50 + "/50").toString();
            photo = R.drawable.ic_tier_forest;
            tier = "숲";
        }
        else if (value >= 20) {
            levelBar.setMax(30);
            levelBar.setProgress(value - 20);
            levelUp = (value-20 + "/30").toString();
            photo = R.drawable.ic_tier_tree;
            tier = "나무";
        }
        else if (value >= 10) {
            levelBar.setMax(10);
            levelBar.setProgress(value - 10);
            levelUp = (value-10 + "/10").toString();
            photo = R.drawable.ic_tier_flower;
            tier = "꽃";
        }
        else if (value >= 1) {
            levelBar.setMax(9);
            levelBar.setProgress(value - 1);
            levelUp = (value-1 + "/9").toString();
            photo = R.drawable.ic_tier_leaf;
            tier = "떡잎";
        }
        else {
            levelBar.setMax(1);
            levelBar.setProgress(value);
            levelUp = (value + "/" + 1).toString();
            photo = R.drawable.ic_tier_seed;
            tier = "씨앗";
        }

        nextLevel.setText(levelUp);
        myTierImageView.setImageResource(photo);
        myTier.setText(tier);
    }
}
