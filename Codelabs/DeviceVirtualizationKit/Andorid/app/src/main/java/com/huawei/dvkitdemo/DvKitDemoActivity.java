/**
 * 本示例用于示范通过DvKit控制虚拟设备的Display,mic,speaker,camera能力。
 */

package com.huawei.dvkitdemo;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huawei.dmsdp.devicevirtualization.Capability;
import com.huawei.dmsdp.devicevirtualization.Constants;
import com.huawei.dmsdp.devicevirtualization.DvKit;
import com.huawei.dmsdp.devicevirtualization.EventType;
import com.huawei.dmsdp.devicevirtualization.IDiscoveryCallback;
import com.huawei.dmsdp.devicevirtualization.IDvKitConnectCallback;
import com.huawei.dmsdp.devicevirtualization.IVirtualDeviceObserver;
import com.huawei.dmsdp.devicevirtualization.VirtualDevice;
import com.huawei.dmsdp.devicevirtualization.VirtualDeviceManager;
import com.huawei.dvkitdemo.camera.RegitsterCameraAPI2;
import com.huawei.dvkitdemoone.R;

import static com.huawei.dmsdp.devicevirtualization.Capability.CAMERA;
import static com.huawei.dmsdp.devicevirtualization.Capability.DISPLAY;
import static com.huawei.dmsdp.devicevirtualization.Capability.MIC;
import static com.huawei.dmsdp.devicevirtualization.Capability.SPEAKER;
import static com.huawei.dmsdp.devicevirtualization.DvKit.VIRTUAL_DEVICE_CLASS;
import static com.huawei.dmsdp.devicevirtualization.ObserverEventType.VIRTUALDEVICE;

public class DvKitDemoActivity extends Activity {
    private static final String TAG = DvKitDemoActivity.class.getSimpleName();

    private static final int DEVICE_ADD = 1;

    private VirtualDeviceManager mVirtualDeviceManager;

    private TextView mLogTextView;

    private ScrollView mScrollView;

    private Button mStartDiscoveryButton;

    private Button mStopDiscoveryButton;

    private ListView mListView = null;

    private VirtualDeviceAdapter mVirtualDeviceAdapter = null;

    /**
     * 设备发现的回调接口，获取发现到的虚拟设备
     */
    private IDiscoveryCallback discoveryCallback = new IDiscoveryCallback() {
        @Override
        public void onFound(VirtualDevice device, int state) {
            if (device == null) {
                addLog("onDevice callback but device is null");
            } else {
                //将发现的设备存储到容器中，并且在界面上显示
                if (!mVirtualDeviceMap.containsKey(device.getDeviceId())) {
                    addLog("onDevice Found: " + device.getDeviceId() + " Name: " + device.getDeviceName() + " Type:"
                        + device.getDeviceType());
                    mVirtualDeviceMap.put(device.getDeviceId(), device);
                    handler.sendMessage(handler.obtainMessage(DEVICE_ADD, device));
                }
            }
        }

        @Override
        public void onState(int state) {

        }
    };

    /**
     * 设备能力状态变化的观察接口，用于获取设备能力的使能去使能状态变化
     */
    private IVirtualDeviceObserver observer = new IVirtualDeviceObserver() {
        //设备状态变化时的回调
        @Override
        public void onDeviceStateChange(VirtualDevice virtualDevice, int returncode) {

        }

        /**
         *设备能力状态变化是的回调
         */
        @Override
        public void onDeviceCapabilityStateChange(VirtualDevice virtualDevice, Capability capability, int returncode) {
            if (returncode == EventType.EVENT_DEVICE_CAPABILITY_ENABLE) {
                //当设备能力使能成功时，应用处理
                onEnable(virtualDevice, capability);
            } else if (returncode == EventType.EVENT_DEVICE_CAPABILITY_DISABLE) {
                //设备能力去使能成功时，应用处理逻辑
                onDisable(virtualDevice, capability);
            } else {
                //设备能力异常时，应用处理逻辑
                onError(virtualDevice, capability, returncode);
            }
        }

        /**
         * 当设备能力使能成功时，应用处理
         */
        public void onEnable(VirtualDevice device, Capability capability) {
            switch (capability) {
                case CAMERA:
                    //Camera使能成功
                    addLog("open camera success");
                    //将camera使能状态设置为true
                    cameraOpend.put(device.getDeviceId(), true);
                    //获取cameraID
                    String data = device.getData(Constants.ANDROID_CAMERAID_FRONT);
                    addLog("open camera " + device.getDeviceName() + " cameraId " + data);
                    //打开虚拟camera
                    Intent intent = new Intent(DvKitDemoActivity.this, RegitsterCameraAPI2.class);
                    intent.putExtra("cameraId", data);
                    Log.i(TAG, "cameraId is " + data);
                    startActivity(intent);
                    break;
                case DISPLAY:
                    //display使能成功，将display使能状态设置为true
                    addLog("open display success");
                    displayOpend.put(device.getDeviceId(), true);
                    break;
                case MIC:
                    //mic使能成功，将mic使能状态设置为true
                    addLog("open mic success");
                    micOpend.put(device.getDeviceId(), true);
                    break;
                case SPEAKER:
                    //speaker使能成功，将speaker使能状态设置为true
                    addLog("open speaker success");
                    speakerOpend.put(device.getDeviceId(), true);
                    break;
            }
        }

        public void onDisable(VirtualDevice device, Capability capability) {
            switch (capability) {
                case CAMERA:
                    //Camera去使能成功，将Camera使能状态设置为false
                    addLog("close camera success");
                    cameraOpend.put(device.getDeviceId(), false);
                    break;
                case DISPLAY:
                    //display去使能成功，将display使能状态设置为false
                    addLog("close display success");
                    displayOpend.put(device.getDeviceId(), false);
                    break;
                case MIC:
                    //mic去使能成功，将mic使能状态设置为false
                    addLog("close mic success");
                    micOpend.put(device.getDeviceId(), false);
                    break;
                case SPEAKER:
                    //speaker去使能成功，将speaker使能状态设置为false
                    addLog("close speaker success");
                    speakerOpend.put(device.getDeviceId(), false);
                    break;
            }
        }

        public void onError(VirtualDevice device, Capability capability, int error) {
            switch (capability) {
                case CAMERA:
                    //Camera状态异常，将Camera使能状态设置为false
                    addLog("camera onError errorcode is " + error);
                    cameraOpend.put(device.getDeviceId(), false);
                    break;
                case DISPLAY:
                    //display状态异常，将display使能状态设置为false
                    addLog("display onError errorcode is " + error);
                    displayOpend.put(device.getDeviceId(), false);
                    break;
                case MIC:
                    //mic状态异常，将mic使能状态设置为false
                    addLog("mic onError errorcode is " + error);
                    micOpend.put(device.getDeviceId(), false);
                    break;
                case SPEAKER:
                    //speaker状态异常，将speaker使能状态设置为false
                    addLog("speaker onError errorcode is " + error);
                    speakerOpend.put(device.getDeviceId(), false);
                    break;
            }
        }
    };

    public static ConcurrentHashMap<String, VirtualDevice> mVirtualDeviceMap = new ConcurrentHashMap<>();

    private static volatile Map<String, Boolean> speakerOpend = new ConcurrentHashMap<>();

    private static volatile Map<String, Boolean> cameraOpend = new ConcurrentHashMap<>();

    private static volatile Map<String, Boolean> micOpend = new ConcurrentHashMap<>();

    private static volatile Map<String, Boolean> displayOpend = new ConcurrentHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化整个view
        initView();
        //初始化DvKit服务
        initMSDPService();

        //设置发现按钮
        mStartDiscoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVirtualDeviceManager == null) {
                    addLog("WechatAdapter is null");
                    return;
                }
                //调用发现接口
                int ret = mVirtualDeviceManager.startDiscovery(discoveryCallback);
                addLog("start discovery result is " + ret);
            }
        });
        //设置取消发现按钮
        mStopDiscoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVirtualDeviceManager == null) {
                    addLog("WechatAdapter is null");
                    return;
                }
                //调用取消发现接口
                int ret = mVirtualDeviceManager.cancelDiscovery(discoveryCallback);
                addLog("stop discovery result is " + ret);
            }
        });
    }

    /**
     * 将发现的设备列出，通过点击设备上的能力按钮可以使能和去使能虚拟设备
     */
    private void initView() {
        setContentView(R.layout.activity_dvkitdemo);

        mStartDiscoveryButton = findViewById(R.id.btn_start_discovery);
        mStopDiscoveryButton = findViewById(R.id.btn_stop_discovery);

        mLogTextView = findViewById(R.id.log);
        mScrollView = findViewById(R.id.scroll);

        mListView = (ListView) findViewById(R.id.listview);
        List<VirtualDeviceData> virtualDeviceDatas = new LinkedList<VirtualDeviceData>();
        mVirtualDeviceAdapter = new VirtualDeviceAdapter(this, virtualDeviceDatas);
        mListView.setAdapter(mVirtualDeviceAdapter);

        if (!mVirtualDeviceMap.isEmpty()) {
            mVirtualDeviceMap.clear();
        }
    }

    /**
     * 初始化虚拟化设备服务
     */
    private void initMSDPService() {
        //连接DvKit服务
        DvKit.getInstance().connect(getApplicationContext(), new IDvKitConnectCallback() {
            //当DvKit服务连接成功时
            @Override
            public void onConnect(int i) {
                addLog("init MSDPService done");
                //获取虚拟设备服务实例
                mVirtualDeviceManager = (VirtualDeviceManager) DvKit.getInstance().getKitService(VIRTUAL_DEVICE_CLASS);
                //注册虚拟设备observer
                mVirtualDeviceManager.subscribe(EnumSet.of(VIRTUALDEVICE), observer);
            }

            @Override
            public void onDisconnect() {

            }
        });
    }

    synchronized void addLog(final String log) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            mLogTextView.append(String.format(Locale.ENGLISH, "\n %s", log));
            scrollToBottom();
        } else {
            mLogTextView.post(new Runnable() {
                @Override
                public void run() {
                    mLogTextView.append(String.format(Locale.ENGLISH, "\n %s", log));
                    scrollToBottom();
                }
            });
        }
    }

    private void scrollToBottom() {
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, mLogTextView.getBottom());
            }
        });
    }

    @Override
    public void onDestroy() {
        DvKit.getInstance().disConnect();
        mVirtualDeviceMap.clear();
        super.onDestroy();
    }

    public class VirtualDeviceData {
        private int imgId;

        private String content;

        private String deviceName;

        public VirtualDeviceData() {
        }

        public VirtualDeviceData(int imgId, String content, String name) {
            this.imgId = imgId;
            this.content = content;
            this.deviceName = name;
        }

        public int getImgId() {
            return imgId;
        }

        public String getContent() {
            return content;
        }

        public String getName() {
            return deviceName;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setName(String name) {
            this.deviceName = name;
        }
    }

    public class VirtualDeviceAdapter extends BaseAdapter {
        private Context mContext;

        private List<VirtualDeviceData> mUpdateData;

        public VirtualDeviceAdapter() {
        }

        public VirtualDeviceAdapter(Context context, List<VirtualDeviceData> UpdateDatas) {
            this.mUpdateData = UpdateDatas;
            this.mContext = context;
        }

        /**
         * 添加列表项
         *
         * @param position
         * @param UpdateData
         */
        public void add(int position, VirtualDeviceData UpdateData) {
            if (null == mUpdateData) {
                mUpdateData = new LinkedList<>();
            }
            mUpdateData.add(position, UpdateData);
            notifyDataSetChanged();
        }

        /**
         * 更新列表内容
         *
         * @param UpdateDatas
         */
        public void update(List<VirtualDeviceData> UpdateDatas) {
            if (null == mUpdateData) {
                mUpdateData = new LinkedList<>();
            }
            mUpdateData.clear();
            mUpdateData.addAll(UpdateDatas);

            notifyDataSetChanged();
        }

        /**
         * 更新列表项
         *
         * @param position
         * @param UpdateData
         */
        public void update(int position, VirtualDeviceData UpdateData) {
            if (mUpdateData != null && position < mUpdateData.size()) {
                mUpdateData.set(position, UpdateData);
            }
            notifyDataSetChanged();
        }

        /**
         * 移除指定列表项
         *
         * @param position
         */
        public void remove(int position) {
            if (mUpdateData != null && 0 != getCount()) {
                mUpdateData.remove(position);
            }
            notifyDataSetChanged();
        }

        /**
         * 清空列表数据
         */
        public void clear() {
            if (mUpdateData != null) {
                mUpdateData.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mUpdateData.size();
        }

        @Override
        public VirtualDeviceData getItem(int position) {
            return mUpdateData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.updatedata, null);
                holder = new ViewHolder();
                holder.img_icon = (ImageView) convertView.findViewById(R.id.icon_img);
                holder.camera_button = (Button) convertView.findViewById(R.id.camera_button);
                holder.display_button = (Button) convertView.findViewById(R.id.display_button);
                holder.speaker_button = (Button) convertView.findViewById(R.id.speaker_button);
                holder.mic_button = (Button) convertView.findViewById(R.id.mic_button);
                holder.name_content = (TextView) convertView.findViewById(R.id.devicename);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            VirtualDeviceData UpdateData = getItem(position);
            holder.img_icon.setImageResource(UpdateData.getImgId());
            holder.name_content.setText(UpdateData.getName());
            holder.camera_button.setText("Camera");
            holder.camera_button.setTag(UpdateData.getContent());
            holder.display_button.setText("Display");
            holder.display_button.setTag(UpdateData.getContent());
            holder.speaker_button.setText("Speaker");
            holder.speaker_button.setTag(UpdateData.getContent());
            holder.mic_button.setText("Mic");
            holder.mic_button.setTag(UpdateData.getContent());

            holder.camera_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String deviceId = (String) v.getTag();
                    VirtualDevice device = mVirtualDeviceMap.get(deviceId);
                    if (device == null) {
                        addLog("do not contains " + deviceId);
                        return;
                    }
                    if (!cameraOpend.containsKey(deviceId) || !cameraOpend.get(deviceId)) {
                        addLog("open camera " + device.getDeviceName());
                        //使能虚拟Camera
                        int ret = mVirtualDeviceManager.enableVirtualDevice(deviceId, EnumSet.of(CAMERA), null);
                        addLog("open camera result is " + ret);
                    } else {
                        addLog("close camera ");
                        //去使能虚拟Camera
                        int ret = mVirtualDeviceManager.disableVirtualDevice(deviceId, EnumSet.of(CAMERA));
                        addLog("close camera result is " + ret);
                    }
                }
            });

            holder.display_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String deviceId = (String) v.getTag();
                    VirtualDevice device = mVirtualDeviceMap.get(deviceId);
                    if (device == null) {
                        addLog("do not contains " + deviceId);
                        return;
                    }
                    if (!displayOpend.containsKey(deviceId) || !displayOpend.get(deviceId)) {
                        addLog("open display " + device.getDeviceName());
                        //使能虚拟Display
                        int ret = mVirtualDeviceManager.enableVirtualDevice(deviceId, EnumSet.of(DISPLAY), null);
                        addLog("open display result is " + ret);
                    } else {
                        addLog("close display ");
                        //去使能虚拟Display
                        int ret = mVirtualDeviceManager.disableVirtualDevice(deviceId, EnumSet.of(DISPLAY));
                        addLog("close display result is " + ret);
                    }
                }
            });

            holder.speaker_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String deviceId = (String) v.getTag();
                    VirtualDevice device = mVirtualDeviceMap.get(deviceId);
                    if (device == null) {
                        addLog("do not contains " + deviceId);
                        return;
                    }
                    if (!speakerOpend.containsKey(deviceId) || !speakerOpend.get(deviceId)) {
                        addLog("open speaker " + device.getDeviceName());
                        //使能虚拟speaker
                        int ret = mVirtualDeviceManager.enableVirtualDevice(deviceId, EnumSet.of(SPEAKER), null);
                        addLog("open speaker result is " + ret);
                    } else {
                        addLog("close speaker " + device.getDeviceName());
                        //去使能虚拟speaker
                        int ret = mVirtualDeviceManager.disableVirtualDevice(deviceId, EnumSet.of(SPEAKER));
                        addLog("close speaker result is " + ret);
                    }
                }
            });

            holder.mic_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String deviceId = (String) v.getTag();
                    VirtualDevice device = mVirtualDeviceMap.get(deviceId);
                    if (device == null) {
                        addLog("do not contains " + deviceId);
                        return;
                    }
                    if (!micOpend.containsKey(deviceId) || !micOpend.get(deviceId)) {
                        addLog("open mic " + device.getDeviceName());
                        //使能虚拟mic
                        int ret = mVirtualDeviceManager.enableVirtualDevice(deviceId, EnumSet.of(MIC), null);
                        addLog("open mic result is " + ret);
                    } else {
                        addLog("close mic");
                        //去使能虚拟mic
                        int ret = mVirtualDeviceManager.disableVirtualDevice(deviceId, EnumSet.of(MIC));
                        addLog("open mic result is " + ret);
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView img_icon;

            Button camera_button;

            Button display_button;

            Button speaker_button;

            Button mic_button;

            TextView name_content;
        }
    }

    @SuppressLint("HandlerLeak")
    private  Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == DEVICE_ADD) {
                int position = mVirtualDeviceAdapter.getCount();
                VirtualDevice virtualDevice = (VirtualDevice) msg.obj;
                mVirtualDeviceAdapter.add(position,
                    new VirtualDeviceData(R.mipmap.ic_launcher, virtualDevice.getDeviceId(),
                        virtualDevice.getDeviceName()));
            }
        }
    };
}
