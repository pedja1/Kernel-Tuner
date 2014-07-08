<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                xmlns:ads="http://schemas.android.com/apk/lib/com.google.ads"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical"
                android:paddingBottom="@dimen/activity_vertical_margin"
                android:paddingLeft="@dimen/activity_horizontal_margin"
                android:paddingRight="@dimen/activity_horizontal_margin"
                android:paddingTop="@dimen/activity_vertical_margin"
                tools:context=".ActivityKernelTuner">

    <LinearLayout
        android:id="@+id/cpu_info_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignLeft="@+id/textView1"
        android:layout_alignParentTop="false"
        android:layout_below="@+id/temperature_layout"
        android:layout_gravity="left|center_vertical"
        android:layout_marginTop="8dp"
        android:baselineAligned="false"
        android:orientation="vertical">


        <TextView
            android:id="@+id/txtCpuLoadText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/txt_cpu_load"/>

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="0.5dp"
            android:src="@color/grey"/>

        <LinearLayout
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:id="@+id/txtCpu0"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/txt_cpu0"
                android:layout_marginTop="8dp"/>

            <TextView
                android:id="@+id/txtCpu2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/txtCpu2"/>

            <TextView
                android:id="@+id/txtCpu1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/txtCpu1"/>

            <TextView
                android:id="@+id/txtCpu3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/txtCpu3"/>


            <ImageView
                android:layout_width="match_parent"
                android:layout_height="0.5dp"
                android:src="@color/grey"/>
        </LinearLayout>

    </LinearLayout>

    <LinearLayout
        android:id="@+id/buttons_layout"
        android:layout_width="match_parent"
        android:layout_height="fill_parent"
        android:layout_above="@+id/ads_layout"
        android:layout_marginTop="8dp"
        android:layout_toLeftOf="@+id/textView1"
        android:orientation="vertical">



        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/btn_cpu"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_gravity="center_vertical"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_cpu"
                        android:singleLine="true"
                        android:text="@string/btn_cpu"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_times"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_gravity="center_vertical"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_times"
                        android:singleLine="true"
                        android:text="@string/btn_times"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_voltage"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_gravity="center_vertical"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_voltage"
                        android:singleLine="true"
                        android:text="@string/btn_voltage"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>


                </LinearLayout>

                <LinearLayout
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/btn_gpu"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_gpu"
                        android:singleLine="true"
                        android:text="@string/btn_gpu"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_mpdecision"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_mp"
                        android:singleLine="true"
                        android:text="@string/btn_mpdecision"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_thermal"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_thermal"
                        android:singleLine="true"
                        android:text="@string/btn_thermal"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>


                </LinearLayout>

                <LinearLayout
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/btn_governor"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_gravity="center_vertical"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_governor"
                        android:singleLine="true"
                        android:text="@string/btn_governor"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_misc"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_misc"
                        android:singleLine="true"
                        android:text="@string/btn_misc"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_task_manager"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_tm"
                        android:singleLine="true"
                        android:text="@string/btn_task_manager"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>


                </LinearLayout>

                <LinearLayout
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/btn_build"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_build"
                        android:singleLine="true"
                        android:text="@string/btn_build"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_sysctl"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_sysctl"
                        android:singleLine="true"
                        android:text="@string/btn_sysctl"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_logcat"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_log"
                        android:singleLine="true"
                        android:text="@string/btn_logcat"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                </LinearLayout>

                <LinearLayout
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/btn_oom"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_oom"
                        android:singleLine="true"
                        android:text="@string/btn_oom"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_sd"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_sd"
                        android:singleLine="true"
                        android:text="@string/btn_sd"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <Button
                        android:id="@+id/btn_profiles"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_profiles"
                        android:singleLine="true"
                        android:text="@string/btn_profiles"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                </LinearLayout>

                <LinearLayout
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <Button
                        android:id="@+id/btn_info"
                        style="?android:attr/buttonStyleSmall"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"
                        android:drawableLeft="@drawable/main_info"
                        android:singleLine="true"
                        android:text="@string/btn_info"
                        android:gravity="left|center_vertical"
                        android:drawablePadding="5dp"
                        android:ellipsize="marquee"
                        android:marqueeRepeatLimit="marquee_forever"
                        android:background="@drawable/default_button_selector"/>

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"/>

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:layout_weight="1"/>
                </LinearLayout>
            </LinearLayout>
        </ScrollView>

    </LinearLayout>

    <LinearLayout
        android:id="@+id/ads_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="false">

        <com.google.ads.AdView
            android:id="@+id/ad"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            ads:adSize="BANNER"
            ads:adUnitId="a15056d54ca9d71"
            ads:testDevices="TEST_EMULATOR, 3CE20BB714ED336822A2508B26BBC32B"
            android:gravity="center_horizontal"/>

        <!-- ads:loadAdOnCreate="true" -->

    </LinearLayout>

    <LinearLayout
        android:id="@+id/toggles_layout"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_above="@+id/ads_layout"
        android:layout_alignLeft="@+id/textView1">

        <Button
            android:id="@+id/btn_cpu1_toggle"
            style="?android:attr/buttonStyleSmall"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="@string/btn_cpu1_toggle"/>

        <Button
            android:id="@+id/btn_cpu2_toggle"
            style="?android:attr/buttonStyleSmall"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="@string/btn_cpu2_toggle"/>

        <Button
            android:id="@+id/btn_cpu3_toggle"
            style="?android:attr/buttonStyleSmall"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="@string/btn_cpu3_toggle"/>
    </LinearLayout>

    <LinearLayout
        android:id="@+id/temperature_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignLeft="@+id/textView1"
        android:layout_alignParentTop="true"
        android:baselineAligned="false"
        android:orientation="horizontal">

        <LinearLayout
            android:id="@+id/temp_cpu_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginRight="5dp"
            android:layout_weight="1"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/txtCpuTempText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:singleLine="true"
                android:text="@string/txtCpuTemp"/>

            <TextView
                android:id="@+id/txtCpuTemp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="end"
                android:singleLine="true"
                android:text="@string/na"/>

        </LinearLayout>

        <LinearLayout
            android:id="@+id/temp_battery_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/txtBatteryTempText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:singleLine="true"
                android:text="@string/txtBatteryTemp"/>

            <TextView
                android:id="@+id/txtBatteryTemp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="end"
                android:singleLine="true"
                android:text="@string/na"/>

        </LinearLayout>
    </LinearLayout>

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="0.5dp"
        android:src="@color/grey"
        android:layout_below="@+id/temperature_layout"
        android:layout_alignLeft="@+id/textView1"/>

    <TextView
        android:id="@+id/textView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/cpu_info_layout"
        android:layout_centerHorizontal="true"/>

</RelativeLayout>