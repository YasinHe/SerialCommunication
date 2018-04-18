package com.qx.interactive.answer.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qx.interactive.answer.R;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class HeadHelperUtils{

    private RelativeLayout rell_header_view;
    private View myHeaderView;
    private LinearLayout lly_back_finish,lly_function_right,lly_function_right2,lly_text_right;
    private ImageView iv_back_finish,iv_logo_position,iv_function_right;
    private TextView tv_set_title,tv_function_right,tv_set_subtitle,tv_center_font;

    public HeadHelperUtils(View view) {
        myHeaderView = view;
        initView();
    }

    private void initView() {
        rell_header_view = (RelativeLayout)myHeaderView.findViewById(R.id.headView);
        lly_back_finish = (LinearLayout)myHeaderView.findViewById(R.id.lly_back_finish);
        lly_function_right = (LinearLayout)myHeaderView.findViewById(R.id.lly_function_right);
        lly_function_right2= (LinearLayout)myHeaderView.findViewById(R.id.lly_function_right2);
        lly_text_right = (LinearLayout)myHeaderView.findViewById(R.id.lly_text_right);
        iv_back_finish = (ImageView)myHeaderView.findViewById(R.id.iv_back_finish);
        iv_logo_position = (ImageView)myHeaderView.findViewById(R.id.iv_logo_position);
        iv_function_right = (ImageView)myHeaderView.findViewById(R.id.iv_function_right);
        tv_set_title = (TextView)myHeaderView.findViewById(R.id.tv_set_title);
        tv_function_right = (TextView)myHeaderView.findViewById(R.id.tv_function_right);
        tv_set_subtitle = (TextView)myHeaderView.findViewById(R.id.tv_set_subtitle);
        tv_center_font = (TextView)myHeaderView.findViewById(R.id.tv_center_font);
    }


    public RelativeLayout getRell_header_view() {
        return rell_header_view;
    }

    public void setRell_header_view(RelativeLayout rell_header_view) {
        this.rell_header_view = rell_header_view;
    }

    public View getMyHeaderView() {
        return myHeaderView;
    }

    public TextView getTv_center_font() {
        return tv_center_font;
    }

    public void setTv_center_font(TextView tv_center_font) {
        this.tv_center_font = tv_center_font;
    }

    public TextView getTv_set_subtitle() {
        return tv_set_subtitle;
    }

    public void setTv_set_subtitle(TextView tv_set_subtitle) {
        this.tv_set_subtitle = tv_set_subtitle;
    }

    public void setMyHeaderView(View myHeaderView) {
        this.myHeaderView = myHeaderView;
    }

    public LinearLayout getLly_back_finish() {
        return lly_back_finish;
    }

    public void setLly_back_finish(LinearLayout lly_back_finish) {
        this.lly_back_finish = lly_back_finish;
    }

    public LinearLayout getLly_function_right() {
        return lly_function_right;
    }

    public void setLly_function_right(LinearLayout lly_function_right) {
        this.lly_function_right = lly_function_right;
    }

    public LinearLayout getLly_text_right() {
        return lly_text_right;
    }

    public void setLly_text_right(LinearLayout lly_text_right) {
        this.lly_text_right = lly_text_right;
    }

    public ImageView getIv_back_finish() {
        return iv_back_finish;
    }

    public void setIv_back_finish(ImageView iv_back_finish) {
        this.iv_back_finish = iv_back_finish;
    }

    public ImageView getIv_logo_position() {
        return iv_logo_position;
    }

    public void setIv_logo_position(ImageView iv_logo_position) {
        this.iv_logo_position = iv_logo_position;
    }

    public ImageView getIv_function_right() {
        return iv_function_right;
    }

    public void setIv_function_right(ImageView iv_function_right) {
        this.iv_function_right = iv_function_right;
    }

    public TextView getTv_set_title() {
        return tv_set_title;
    }

    public void setTv_set_title(TextView tv_set_title) {
        this.tv_set_title = tv_set_title;
    }

    public TextView getTv_function_right() {
        return tv_function_right;
    }

    public void setTv_function_right(TextView tv_function_right) {
        this.tv_function_right = tv_function_right;
    }

    public LinearLayout getLly_function_right2() {
        return lly_function_right2;
    }

    public void setLly_function_right2(LinearLayout lly_function_right2) {
        this.lly_function_right2 = lly_function_right2;
    }
}
