package cn.xiaowine.dsp.data

enum class MODE {

    /**
     * application.getSharedPreferences(key, Context.MODE_PRIVATE)
     */
    APP,

    /**
     *  application.createDeviceProtectedStorageContext().getSharedPreferences(key, Context.MODE_PRIVATE)
     *  Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
     */
    HOOK
}