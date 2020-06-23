package com.lr.aodiu.hapticskitdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.devices.hapticskit.HapticsKit;
import com.huawei.devices.hapticskit.HapticsKitAdapter;
import com.huawei.devices.utils.HapticsKitConstant;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    HapticsKit kit;
    HapticsKitAdapter adapter;
    private RecyclerView mList;
    private ArrayList<MenuBean> data;
    private Button btn1;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        text = (TextView) findViewById(R.id.textView);
        text.setText(this.getPackageName());
        kit = new HapticsKit(this);
        adapter = kit.initialize(1);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = adapter.getParameter(HapticsKitConstant.HW_HAPTIC_DIRECTION_VALUE);
                text.setText(result);
                //result = adapter.getParameter(VibratorKitConstant.HW_HAPTIC_GRADE_VALUE);
                //text.setText(result);
                //text.setText(android.os.Build.MODEL);

            }
        });
        mList = (RecyclerView) findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        data = new ArrayList<>();
        add("Camera_enum.CLICK", HapticsKitConstant.CameraTypeEnum.CLICK.getType());
        add("Camera_enum.CLICK_UP", HapticsKitConstant.CameraTypeEnum.CLICK_UP.getType());
        add("Camera_enum.FOCUS",HapticsKitConstant.CameraTypeEnum.FOCUS.getType());
        add("Camera_enum.GEAR_SLIP",HapticsKitConstant.CameraTypeEnum.GEAR_SLIP.getType());
        add("Camera_enum.LONG_PRESS",HapticsKitConstant.CameraTypeEnum.LONG_PRESS.getType());
        add("Camera_enum.MODE_SWITCH",HapticsKitConstant.CameraTypeEnum.MODE_SWITCH.getType());
        add("Camera_enum.PORTAIT_SWITH",HapticsKitConstant.CameraTypeEnum.PORTAIT_SWITH.getType());
        add("Battery.CHARGING",HapticsKitConstant.BatteryTypeEnum.CHARGING.getType());
        add("Calculator.DELETE",HapticsKitConstant.CalculatorTypeEnum.DELETE_LONG_PRESS.getType());
        add("Calculator.VITUAL_TASK",HapticsKitConstant.CalculatorTypeEnum.VITUAL_TASK.getType());
        add("Clock.STOPWATCH",HapticsKitConstant.ClockTypeEnum.STOPWATCH.getType());
        add("Clock.Timer",HapticsKitConstant.ClockTypeEnum.TIMER.getType());
        add("Contact.DELTE",HapticsKitConstant.DiallerTypeEnum.DELTE.getType());
        add("Contact.LETTERS_INDEX",HapticsKitConstant.DiallerTypeEnum.LETTERS_INDEX.getType());
        add("Contact.LONGPRESS",HapticsKitConstant.DiallerTypeEnum.LONGPRESS.getType());
        add("Contact.CLICK",HapticsKitConstant.DiallerTypeEnum.CLICK.getType());
        add("Control.SEARCH_LONG_PRESS",HapticsKitConstant.ControlTypeEnum.SEARCH_LONG_PRESS.getType());
        add("Control.TEXT_CHOSE",HapticsKitConstant.ControlTypeEnum.TEXT_CHOSE_CURSOR_MOVE.getType());
        add("Control.TEXT_EDIT",HapticsKitConstant.ControlTypeEnum.TEXT_EDIT.getType());
        add("Control.WIDGET",HapticsKitConstant.ControlTypeEnum.WIDGET_OPERATION.getType());
        add("Desktop.LONG_PRESS",HapticsKitConstant.DesktopTypeEnum.LONG_PRESS.getType());
        add("FingerPrint.INPUT",HapticsKitConstant.FingerPrintTypeEnum.INPUT_LONG_PRESS.getType());
        add("FingerPrint.UNLOCK_FAILE",HapticsKitConstant.FingerPrintTypeEnum.UNLOCK_FAILE.getType());
        add("Gallery.ALBUMS",HapticsKitConstant.GalleryTypeEnum.ALBUMS_LONG_PRESS.getType());
        add("Gallery.PHOTO",HapticsKitConstant.GalleryTypeEnum.PHOTOS_LONG_PRESS.getType());
        add("Gallery.UPGLIDES",HapticsKitConstant.GalleryTypeEnum.UPGLIDE_RELATED.getType());
        add("Home.CLICK_BACK.",HapticsKitConstant.NavigationTypeEnum.CLICK_BACK.getType());
        add("Home.CLICK_HOME.",HapticsKitConstant.NavigationTypeEnum.CLICK_HOME.getType());
        add("Home.CLICK_MULTITASK.",HapticsKitConstant.NavigationTypeEnum.CLICK_MULTITASK.getType());
        add("LockScreen.FACE_UNLOCK_FAIL.",HapticsKitConstant.LockScreenTypeEnum.FACE_UNLOCK_FAIL.getType());
        add("LockScreen.FACE_UNLOCK_RETRY.",HapticsKitConstant.LockScreenTypeEnum.FACE_UNLOCK_RETRY.getType());
        add("LockScreen.NUMBER_UNLOCK_FAIL.",HapticsKitConstant.LockScreenTypeEnum.NUMBER_UNLOCK_FAIL.getType());
        add("LockScreen.UNLOCK_CLICK.",HapticsKitConstant.LockScreenTypeEnum.UNLOCK_CLICK.getType());
        add("LockScreen.ONEHAND.",HapticsKitConstant.LockScreenTypeEnum.ONEHAND_KEYBOARD_SWITCH.getType());
        add("LockScreen.UNLOCK_SLIP.",HapticsKitConstant.LockScreenTypeEnum.UNLOCK_SLIP.getType());
        add("LockScreen.UPGLIDE_SWITCH.",HapticsKitConstant.LockScreenTypeEnum.UPGLIDE_SWITCH.getType());
        add("SystemUI.EXPAND.",HapticsKitConstant.SystemuiTypeEnum.EXPAND.getType());
        add("SystemUI.MOVE.",HapticsKitConstant.SystemuiTypeEnum.MOVE.getType());
        add("SystemUI.NO_LONG_PRESS.",HapticsKitConstant.SystemuiTypeEnum.NOTIFICATIONS_LONG_PRESS.getType());
        add("SystemUI.SCREEN_RECORD.",HapticsKitConstant.SystemuiTypeEnum.SCREEN_RECORD_STOP.getType());
        add("SystemUI.SWITCH_PRESS.",HapticsKitConstant.SystemuiTypeEnum.SWITCH_LONG_PRESS.getType());
        add("SystemUI.SWITCH_SORT_PRESS.",HapticsKitConstant.SystemuiTypeEnum.SWITCH_SORT_LONG_PRESS.getType());
        add("Wallet.TIME_SCROLL.",HapticsKitConstant.WalletTypeEnum.TIME_SCROLL.getType());

        add("百度输入法定制强度 1",HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH1);
        add("百度输入法定制强度 2",HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH2);
        add("百度输入法定制强度 3",HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH3);
        add("百度输入法定制强度 4",HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH4);
        add("百度输入法定制强度 5",HapticsKitConstant.HW_HAPTIC_TYPE_GRADE_STRENGTH5);
        mList.setAdapter(new MenuAdapter());
    }

    private void add(String name, String type) {
        MenuBean bean = new MenuBean();
        bean.name = name;
        bean.type = type;
        bean.color = Color.BLACK;
        data.add(bean);
    }

    private void add(String name, String type, int color) {
        MenuBean bean = new MenuBean();
        bean.name = name;
        bean.type = type;
        bean.color = color;
        data.add(bean);
    }

    private class MenuBean {
        String name;
        String type;
        int color;
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {
        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder {
            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn = (Button) itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MainActivity.this);
            }

            public void setPosition(int Position) {
                MenuBean bean = data.get(Position);
                mBtn.setText(bean.name);
                mBtn.setTextColor(bean.color);
                mBtn.setTag(Position);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        MenuBean bean = data.get(position);
        adapter.setParameter(bean.type);
        text.setText(bean.type);
    }
}
