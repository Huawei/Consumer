package com.huawei.hiaicodedemo.widget.cropview;


import android.graphics.Path;

public class InRegion {

    public static void contiansPoint(Lasso region1, Lasso region2, Lasso region3, Lasso region4, Path path) {
        if (region1.contains(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY()) || region1.contains(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY())) {
            if (region1.contains(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY())) {
                int top_left_x = CornerPoint.TOP_LEFT.getPointX();
                int top_left_y = CornerPoint.TOP_LEFT.getPointY();

                int bottom_left_x = CornerPoint.BOTTOM_LEFT.getPointX();
                int bottom_left_y = CornerPoint.BOTTOM_LEFT.getPointY();


                CornerPoint.TOP_LEFT.setPointX(bottom_left_x);
                CornerPoint.TOP_LEFT.setPointY(bottom_left_y);
                CornerPoint.BOTTOM_LEFT.setPointX(top_left_x);
                CornerPoint.BOTTOM_LEFT.setPointY(top_left_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();

                CornerPoint.TOP_LEFT.setClick(true);
                CornerPoint.BOTTOM_LEFT.setClick(false);
               // Log.e("tag","1");
                return;
            } else {
                int top_left_x = CornerPoint.TOP_LEFT.getPointX();
                int top_left_y = CornerPoint.TOP_LEFT.getPointY();

                int top_right_x = CornerPoint.TOP_RIGHT.getPointX();
                int top_right_y = CornerPoint.TOP_RIGHT.getPointY();


                CornerPoint.TOP_LEFT.setPointX(top_right_x);
                CornerPoint.TOP_LEFT.setPointY(top_right_y);
                CornerPoint.TOP_RIGHT.setPointX(top_left_x);
                CornerPoint.TOP_RIGHT.setPointY(top_left_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();
                CornerPoint.TOP_LEFT.setClick(true);
                CornerPoint.TOP_RIGHT.setClick(false);
              //  Log.e("tag","2");
                return;
            }

        }
        if (region2.contains(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY()) || region2.contains(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY())) {
            if (region2.contains(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY())) {
                int top_left_x = CornerPoint.TOP_LEFT.getPointX();
                int top_left_y = CornerPoint.TOP_LEFT.getPointY();

                int top_right_x = CornerPoint.TOP_RIGHT.getPointX();
                int top_right_y = CornerPoint.TOP_RIGHT.getPointY();


                CornerPoint.TOP_LEFT.setPointX(top_right_x);
                CornerPoint.TOP_LEFT.setPointY(top_right_y);
                CornerPoint.TOP_RIGHT.setPointX(top_left_x);
                CornerPoint.TOP_RIGHT.setPointY(top_left_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();
                CornerPoint.TOP_LEFT.setClick(false);
                CornerPoint.TOP_RIGHT.setClick(true);
             //   Log.e("tag","3");
                return;
            } else {
                int top_right_x = CornerPoint.TOP_RIGHT.getPointX();
                int top_right_y = CornerPoint.TOP_RIGHT.getPointY();

                int bottom_right_x = CornerPoint.BOTTOM_RIGHT.getPointX();
                int bottom_right_y = CornerPoint.BOTTOM_RIGHT.getPointY();


                CornerPoint.TOP_RIGHT.setPointX(bottom_right_x);
                CornerPoint.TOP_RIGHT.setPointY(bottom_right_y);
                CornerPoint.BOTTOM_RIGHT.setPointX(top_right_x);
                CornerPoint.BOTTOM_RIGHT.setPointY(top_right_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();
                CornerPoint.TOP_RIGHT.setClick(true);
                CornerPoint.BOTTOM_RIGHT.setClick(false);
             //   Log.e("tag","4");
                return;
            }

        }
        if (region3.contains(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY()) || region3.contains(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY())) {
            if (region3.contains(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY())) {
                int top_left_x = CornerPoint.TOP_LEFT.getPointX();
                int top_left_y = CornerPoint.TOP_LEFT.getPointY();

                int bottom_left_x = CornerPoint.BOTTOM_LEFT.getPointX();
                int bottom_left_y = CornerPoint.BOTTOM_LEFT.getPointY();


                CornerPoint.TOP_LEFT.setPointX(bottom_left_x);
                CornerPoint.TOP_LEFT.setPointY(bottom_left_y);
                CornerPoint.BOTTOM_LEFT.setPointX(top_left_x);
                CornerPoint.BOTTOM_LEFT.setPointY(top_left_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();

                CornerPoint.TOP_LEFT.setClick(false);
                CornerPoint.BOTTOM_LEFT.setClick(true);
              //  Log.e("tag","5");
                return;
            } else {
                int bottom_right_x = CornerPoint.BOTTOM_RIGHT.getPointX();
                int bottom_right_y = CornerPoint.BOTTOM_RIGHT.getPointY();

                int bottom_left_x = CornerPoint.BOTTOM_LEFT.getPointX();
                int bottom_left_y = CornerPoint.BOTTOM_LEFT.getPointY();


                CornerPoint.BOTTOM_RIGHT.setPointX(bottom_left_x);
                CornerPoint.BOTTOM_RIGHT.setPointY(bottom_left_y);
                CornerPoint.BOTTOM_LEFT.setPointX(bottom_right_x);
                CornerPoint.BOTTOM_LEFT.setPointY(bottom_right_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();

                CornerPoint.BOTTOM_RIGHT.setClick(false);
                CornerPoint.BOTTOM_LEFT.setClick(true);
              //  Log.e("tag","6");
                return;
            }

        }
        if (region4.contains(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY()) || region4.contains(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY())) {
            if (region4.contains(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY())) {
                int bottom_right_x = CornerPoint.BOTTOM_RIGHT.getPointX();
                int bottom_right_y = CornerPoint.BOTTOM_RIGHT.getPointY();

                int bottom_left_x = CornerPoint.BOTTOM_LEFT.getPointX();
                int bottom_left_y = CornerPoint.BOTTOM_LEFT.getPointY();


                CornerPoint.BOTTOM_RIGHT.setPointX(bottom_left_x);
                CornerPoint.BOTTOM_RIGHT.setPointY(bottom_left_y);
                CornerPoint.BOTTOM_LEFT.setPointX(bottom_right_x);
                CornerPoint.BOTTOM_LEFT.setPointY(bottom_right_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();

                CornerPoint.BOTTOM_RIGHT.setClick(true);
                CornerPoint.BOTTOM_LEFT.setClick(false);
               // Log.e("tag","7");
                return;
            } else {
                int top_right_x = CornerPoint.TOP_RIGHT.getPointX();
                int top_right_y = CornerPoint.TOP_RIGHT.getPointY();

                int bottom_right_x = CornerPoint.BOTTOM_RIGHT.getPointX();
                int bottom_right_y = CornerPoint.BOTTOM_RIGHT.getPointY();


                CornerPoint.TOP_RIGHT.setPointX(bottom_right_x);
                CornerPoint.TOP_RIGHT.setPointY(bottom_right_y);
                CornerPoint.BOTTOM_RIGHT.setPointX(top_right_x);
                CornerPoint.BOTTOM_RIGHT.setPointY(top_right_y);

                path.reset();
                path.moveTo(CornerPoint.TOP_LEFT.getPointX(), CornerPoint.TOP_LEFT.getPointY());
                path.lineTo(CornerPoint.TOP_RIGHT.getPointX(), CornerPoint.TOP_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_RIGHT.getPointX(), CornerPoint.BOTTOM_RIGHT.getPointY());
                path.lineTo(CornerPoint.BOTTOM_LEFT.getPointX(), CornerPoint.BOTTOM_LEFT.getPointY());
                path.close();

                CornerPoint.TOP_RIGHT.setClick(false);
                CornerPoint.BOTTOM_RIGHT.setClick(true);
              //  Log.e("tag","8");
                return;
            }

        }
    }
}
