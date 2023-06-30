package com.ecochallenge.sns_project.listener;

import com.ecochallenge.sns_project.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
